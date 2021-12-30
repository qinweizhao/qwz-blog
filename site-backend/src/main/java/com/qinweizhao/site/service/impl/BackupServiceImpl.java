package com.qinweizhao.site.service.impl;

import static com.qinweizhao.site.model.support.HaloConst.HALO_BACKUP_MARKDOWN_PREFIX;
import static com.qinweizhao.site.model.support.HaloConst.HALO_BACKUP_PREFIX;
import static com.qinweizhao.site.model.support.HaloConst.HALO_DATA_EXPORT_PREFIX;
import static com.qinweizhao.site.utils.DateTimeUtils.HORIZONTAL_LINE_DATETIME_FORMATTER;
import static com.qinweizhao.site.utils.FileUtils.checkDirectoryTraversal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import com.qinweizhao.site.config.properties.HaloProperties;
import com.qinweizhao.site.event.options.OptionUpdatedEvent;
import com.qinweizhao.site.event.theme.ThemeUpdatedEvent;
import com.qinweizhao.site.exception.BadRequestException;
import com.qinweizhao.site.exception.NotFoundException;
import com.qinweizhao.site.exception.ServiceException;
import com.qinweizhao.site.handler.file.FileHandler;
import com.qinweizhao.site.model.dto.BackupDTO;
import com.qinweizhao.site.model.dto.post.BasePostDetailDTO;
import com.qinweizhao.site.model.entity.Attachment;
import com.qinweizhao.site.model.entity.Category;
import com.qinweizhao.site.model.entity.CommentBlackList;
import com.qinweizhao.site.model.entity.Journal;
import com.qinweizhao.site.model.entity.JournalComment;
import com.qinweizhao.site.model.entity.Link;
import com.qinweizhao.site.model.entity.Log;
import com.qinweizhao.site.model.entity.Menu;
import com.qinweizhao.site.model.entity.Option;
import com.qinweizhao.site.model.entity.Photo;
import com.qinweizhao.site.model.entity.Post;
import com.qinweizhao.site.model.entity.PostCategory;
import com.qinweizhao.site.model.entity.PostComment;
import com.qinweizhao.site.model.entity.PostMeta;
import com.qinweizhao.site.model.entity.PostTag;
import com.qinweizhao.site.model.entity.Sheet;
import com.qinweizhao.site.model.entity.SheetComment;
import com.qinweizhao.site.model.entity.SheetMeta;
import com.qinweizhao.site.model.entity.Tag;
import com.qinweizhao.site.model.entity.ThemeSetting;
import com.qinweizhao.site.model.entity.User;
import com.qinweizhao.site.model.params.PostMarkdownParam;
import com.qinweizhao.site.model.support.HaloConst;
import com.qinweizhao.site.model.vo.PostMarkdownVO;
import com.qinweizhao.site.security.service.OneTimeTokenService;
import com.qinweizhao.site.service.AttachmentService;
import com.qinweizhao.site.service.BackupService;
import com.qinweizhao.site.service.CategoryService;
import com.qinweizhao.site.service.CommentBlackListService;
import com.qinweizhao.site.service.JournalCommentService;
import com.qinweizhao.site.service.JournalService;
import com.qinweizhao.site.service.LinkService;
import com.qinweizhao.site.service.LogService;
import com.qinweizhao.site.service.MenuService;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.service.PhotoService;
import com.qinweizhao.site.service.PostCategoryService;
import com.qinweizhao.site.service.PostCommentService;
import com.qinweizhao.site.service.PostMetaService;
import com.qinweizhao.site.service.PostService;
import com.qinweizhao.site.service.PostTagService;
import com.qinweizhao.site.service.SheetCommentService;
import com.qinweizhao.site.service.SheetMetaService;
import com.qinweizhao.site.service.SheetService;
import com.qinweizhao.site.service.TagService;
import com.qinweizhao.site.service.ThemeSettingService;
import com.qinweizhao.site.service.UserService;
import com.qinweizhao.site.utils.DateTimeUtils;
import com.qinweizhao.site.utils.DateUtils;
import com.qinweizhao.site.utils.FileUtils;
import com.qinweizhao.site.utils.HaloUtils;
import com.qinweizhao.site.utils.JsonUtils;

/**
 * Backup service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author Raremaa
 * @author guqing
 * @date  2019-04-26
 */
@Service
@Slf4j
public class BackupServiceImpl implements BackupService {

    private static final String BACKUP_RESOURCE_BASE_URI = "/api/admin/backups/work-dir";

    private static final String DATA_EXPORT_MARKDOWN_BASE_URI =
        "/api/admin/backups/markdown/export";

    private static final String DATA_EXPORT_BASE_URI = "/api/admin/backups/data";

    private static final String UPLOAD_SUB_DIR = "upload/";

    private final AttachmentService attachmentService;

    private final CategoryService categoryService;

    private final CommentBlackListService commentBlackListService;

    private final JournalService journalService;

    private final JournalCommentService journalCommentService;

    private final LinkService linkService;

    private final LogService logService;

    private final MenuService menuService;

    private final OptionService optionService;

    private final PhotoService photoService;

    private final PostService postService;

    private final PostCategoryService postCategoryService;

    private final PostCommentService postCommentService;

    private final PostMetaService postMetaService;

    private final PostTagService postTagService;

    private final SheetService sheetService;

    private final SheetCommentService sheetCommentService;

    private final SheetMetaService sheetMetaService;

    private final TagService tagService;

    private final ThemeSettingService themeSettingService;

    private final UserService userService;

    private final OneTimeTokenService oneTimeTokenService;

    private final HaloProperties haloProperties;

    private final ApplicationEventPublisher eventPublisher;

    public BackupServiceImpl(AttachmentService attachmentService, CategoryService categoryService,
        CommentBlackListService commentBlackListService, JournalService journalService,
        JournalCommentService journalCommentService, LinkService linkService, LogService logService,
        MenuService menuService, OptionService optionService, PhotoService photoService,
        PostService postService, PostCategoryService postCategoryService,
        PostCommentService postCommentService, PostMetaService postMetaService,
        PostTagService postTagService, SheetService sheetService,
        SheetCommentService sheetCommentService, SheetMetaService sheetMetaService,
        TagService tagService, ThemeSettingService themeSettingService, UserService userService,
        OneTimeTokenService oneTimeTokenService, HaloProperties haloProperties,
        ApplicationEventPublisher eventPublisher) {
        this.attachmentService = attachmentService;
        this.categoryService = categoryService;
        this.commentBlackListService = commentBlackListService;
        this.journalService = journalService;
        this.journalCommentService = journalCommentService;
        this.linkService = linkService;
        this.logService = logService;
        this.menuService = menuService;
        this.optionService = optionService;
        this.photoService = photoService;
        this.postService = postService;
        this.postCategoryService = postCategoryService;
        this.postCommentService = postCommentService;
        this.postMetaService = postMetaService;
        this.postTagService = postTagService;
        this.sheetService = sheetService;
        this.sheetCommentService = sheetCommentService;
        this.sheetMetaService = sheetMetaService;
        this.tagService = tagService;
        this.themeSettingService = themeSettingService;
        this.userService = userService;
        this.oneTimeTokenService = oneTimeTokenService;
        this.haloProperties = haloProperties;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BasePostDetailDTO importMarkdown(MultipartFile file) throws IOException {

        // Read markdown content.
        String markdown = FileUtils.readString(file.getInputStream());

        // TODO sheet import
        return postService.importMarkdown(markdown, file.getOriginalFilename());
    }

    @Override
    public BackupDTO backupWorkDirectory(List<String> options) {
        if (CollectionUtils.isEmpty(options)) {
            throw new BadRequestException("The options parameter is missing, at least one.");
        }
        // Zip work directory to temporary file
        try {
            // Create zip path for halo zip
            String haloZipFileName = HALO_BACKUP_PREFIX
                + DateTimeUtils.format(LocalDateTime.now(), HORIZONTAL_LINE_DATETIME_FORMATTER)
                + HaloUtils.simpleUUID().hashCode() + ".zip";
            // Create halo zip file
            Path haloZipFilePath = Paths.get(haloProperties.getBackupDir(), haloZipFileName);
            if (!Files.exists(haloZipFilePath.getParent())) {
                Files.createDirectories(haloZipFilePath.getParent());
            }
            Path haloZipPath = Files.createFile(haloZipFilePath);

            // Zip halo
            com.qinweizhao.site.utils.FileUtils
                .zip(Paths.get(this.haloProperties.getWorkDir()), haloZipPath,
                    path -> {
                        for (String itemToBackup : options) {
                            Path backupItemPath =
                                Paths.get(this.haloProperties.getWorkDir()).resolve(itemToBackup);
                            if (path.startsWith(backupItemPath)) {
                                return true;
                            }
                        }
                        return false;
                    });

            // Build backup dto
            return buildBackupDto(BACKUP_RESOURCE_BASE_URI, haloZipPath);
        } catch (IOException e) {
            throw new ServiceException("Failed to backup halo", e);
        }
    }

    @Override
    public List<BackupDTO> listWorkDirBackups() {
        // Ensure the parent folder exist
        Path backupParentPath = Paths.get(haloProperties.getBackupDir());
        if (Files.notExists(backupParentPath)) {
            return Collections.emptyList();
        }

        // Build backup dto
        try (Stream<Path> subPathStream = Files.list(backupParentPath)) {
            return subPathStream
                .filter(backupPath -> StringUtils
                    .startsWithIgnoreCase(backupPath.getFileName().toString(),
                        HALO_BACKUP_PREFIX))
                .map(backupPath -> buildBackupDto(BACKUP_RESOURCE_BASE_URI, backupPath))
                .sorted(Comparator.comparingLong(BackupDTO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch backups", e);
        }
    }

    @Override
    public Optional<BackupDTO> getBackup(@NonNull Path backupFilePath, @NonNull BackupType type) {
        if (Files.notExists(backupFilePath)) {
            return Optional.empty();
        }

        BackupDTO backupDto = buildBackupDto(type.getBaseUri(), backupFilePath);
        return Optional.of(backupDto);
    }

    @Override
    public void deleteWorkDirBackup(String fileName) {
        Assert.hasText(fileName, "File name must not be blank");

        Path backupRootPath = Paths.get(haloProperties.getBackupDir());

        // Get backup path
        Path backupPath = backupRootPath.resolve(fileName);

        // Check directory traversal
        checkDirectoryTraversal(backupRootPath, backupPath);

        try {
            // Delete backup file
            Files.delete(backupPath);
        } catch (NoSuchFileException e) {
            throw new NotFoundException("The file " + fileName + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to delete backup", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String basePath, String fileName) {
        Assert.hasText(basePath, "Base path must not be blank");
        Assert.hasText(fileName, "Backup file name must not be blank");

        Path backupParentPath = Paths.get(basePath);

        try {
            if (Files.notExists(backupParentPath)) {
                // Create backup parent path if it does not exists
                Files.createDirectories(backupParentPath);
            }

            // Get backup file path
            Path backupFilePath = Paths.get(basePath, fileName).normalize();

            // Check directory traversal
            checkDirectoryTraversal(backupParentPath, backupFilePath);

            // Build url resource
            Resource backupResource = new UrlResource(backupFilePath.toUri());
            if (!backupResource.exists()) {
                // If the backup resource is not exist
                throw new NotFoundException("The file " + fileName + " was not found");
            }
            // Return the backup resource
            return backupResource;
        } catch (MalformedURLException e) {
            throw new NotFoundException("The file " + fileName + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to create backup parent path: " + backupParentPath,
                e);
        }
    }

    @Override
    public BackupDTO exportData() {
        Map<String, Object> data = new HashMap<>();
        data.put("version", HaloConst.HALO_VERSION);
        data.put("export_date", DateUtils.now());
        data.put("attachments", attachmentService.listAll());
        data.put("categories", categoryService.listAll(true));
        data.put("comment_black_list", commentBlackListService.listAll());
        data.put("journals", journalService.listAll());
        data.put("journal_comments", journalCommentService.listAll());
        data.put("links", linkService.listAll());
        data.put("logs", logService.listAll());
        data.put("menus", menuService.listAll());
        data.put("options", optionService.listAll());
        data.put("photos", photoService.listAll());
        data.put("posts", postService.listAll());
        data.put("post_categories", postCategoryService.listAll());
        data.put("post_comments", postCommentService.listAll());
        data.put("post_metas", postMetaService.listAll());
        data.put("post_tags", postTagService.listAll());
        data.put("sheets", sheetService.listAll());
        data.put("sheet_comments", sheetCommentService.listAll());
        data.put("sheet_metas", sheetMetaService.listAll());
        data.put("tags", tagService.listAll());
        data.put("theme_settings", themeSettingService.listAll());
        data.put("user", userService.listAll());

        try {
            String haloDataFileName = HALO_DATA_EXPORT_PREFIX
                + DateTimeUtils.format(LocalDateTime.now(), HORIZONTAL_LINE_DATETIME_FORMATTER)
                + HaloUtils.simpleUUID().hashCode() + ".json";

            Path haloDataFilePath = Paths.get(haloProperties.getDataExportDir(), haloDataFileName);
            if (!Files.exists(haloDataFilePath.getParent())) {
                Files.createDirectories(haloDataFilePath.getParent());
            }
            Path haloDataPath = Files.createFile(haloDataFilePath);

            FileUtils.writeStringToFile(haloDataPath.toFile(), JsonUtils.objectToJson(data));
            return buildBackupDto(DATA_EXPORT_BASE_URI, haloDataPath);
        } catch (IOException e) {
            throw new ServiceException("导出数据失败", e);
        }
    }

    @Override
    public List<BackupDTO> listExportedData() {

        Path exportedDataParentPath = Paths.get(haloProperties.getDataExportDir());
        if (Files.notExists(exportedDataParentPath)) {
            return Collections.emptyList();
        }

        try (Stream<Path> subPathStream = Files.list(exportedDataParentPath)) {
            return subPathStream
                .filter(backupPath -> StringUtils
                    .startsWithIgnoreCase(backupPath.getFileName().toString(),
                        HALO_DATA_EXPORT_PREFIX))
                .map(backupPath -> buildBackupDto(DATA_EXPORT_BASE_URI, backupPath))
                .sorted(Comparator.comparingLong(BackupDTO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch exported data", e);
        }
    }

    @Override
    public void deleteExportedData(String fileName) {
        Assert.hasText(fileName, "File name must not be blank");

        Path dataExportRootPath = Paths.get(haloProperties.getDataExportDir());

        Path backupPath = dataExportRootPath.resolve(fileName);

        checkDirectoryTraversal(dataExportRootPath, backupPath);

        try {
            // Delete backup file
            Files.delete(backupPath);
        } catch (NoSuchFileException e) {
            throw new NotFoundException("The file " + fileName + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to delete backup", e);
        }
    }

    @Override
    public void importData(MultipartFile file) throws IOException {
        String jsonContent = FileUtils.readString(file.getInputStream());

        ObjectMapper mapper = JsonUtils.createDefaultJsonMapper();
        TypeReference<HashMap<String, Object>> typeRef =
            new TypeReference<>() {
            };
        HashMap<String, Object> data = mapper.readValue(jsonContent, typeRef);

        List<Attachment> attachments = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("attachments")), Attachment[].class));
        attachmentService.createInBatch(attachments);

        List<Category> categories = Arrays.asList(
            mapper.readValue(mapper.writeValueAsString(data.get("categories")), Category[].class));
        categoryService.createInBatch(categories);

        List<Tag> tags = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("tags")), Tag[].class));
        tagService.createInBatch(tags);

        List<CommentBlackList> commentBlackList = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("comment_black_list")),
                CommentBlackList[].class));
        commentBlackListService.createInBatch(commentBlackList);

        List<Journal> journals = Arrays.asList(
            mapper.readValue(mapper.writeValueAsString(data.get("journals")), Journal[].class));
        journalService.createInBatch(journals);

        List<JournalComment> journalComments = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("journal_comments")),
                JournalComment[].class));
        journalCommentService.createInBatch(journalComments);

        List<Link> links = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("links")), Link[].class));
        linkService.createInBatch(links);

        List<Log> logs = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("logs")), Log[].class));
        logService.createInBatch(logs);

        List<Menu> menus = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("menus")), Menu[].class));
        menuService.createInBatch(menus);

        List<Option> options = Arrays.asList(
            mapper.readValue(mapper.writeValueAsString(data.get("options")), Option[].class));
        optionService.createInBatch(options);

        eventPublisher.publishEvent(new OptionUpdatedEvent(this));

        List<Photo> photos = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("photos")), Photo[].class));
        photoService.createInBatch(photos);

        List<Post> posts = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("posts")), Post[].class));
        postService.createInBatch(posts);

        List<PostCategory> postCategories = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("post_categories")),
                PostCategory[].class));
        postCategoryService.createInBatch(postCategories);

        List<PostComment> postComments = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("post_comments")), PostComment[].class));
        postCommentService.createInBatch(postComments);

        List<PostMeta> postMetas = Arrays.asList(
            mapper.readValue(mapper.writeValueAsString(data.get("post_metas")), PostMeta[].class));
        postMetaService.createInBatch(postMetas);

        List<PostTag> postTags = Arrays.asList(
            mapper.readValue(mapper.writeValueAsString(data.get("post_tags")), PostTag[].class));
        postTagService.createInBatch(postTags);

        List<Sheet> sheets = Arrays
            .asList(mapper.readValue(mapper.writeValueAsString(data.get("sheets")), Sheet[].class));
        sheetService.createInBatch(sheets);

        List<SheetComment> sheetComments = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("sheet_comments")),
                SheetComment[].class));
        sheetCommentService.createInBatch(sheetComments);

        List<SheetMeta> sheetMetas = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("sheet_metas")), SheetMeta[].class));
        sheetMetaService.createInBatch(sheetMetas);

        List<ThemeSetting> themeSettings = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("theme_settings")),
                ThemeSetting[].class));
        themeSettingService.createInBatch(themeSettings);

        eventPublisher.publishEvent(new ThemeUpdatedEvent(this));

        List<User> users = Arrays.asList(mapper
            .readValue(mapper.writeValueAsString(data.get("user")),
                User[].class));

        if (users.size() > 0) {
            userService.create(users.get(0));
        }
    }

    @Override
    public BackupDTO exportMarkdowns(PostMarkdownParam postMarkdownParam) throws IOException {
        // Query all Post data
        List<PostMarkdownVO> postMarkdownList = postService.listPostMarkdowns();
        Assert.notEmpty(postMarkdownList, "当前无文章可以导出");

        // Write files to the temporary directory
        String markdownFileTempPathName =
            haloProperties.getBackupMarkdownDir() + HaloUtils.simpleUUID().hashCode();
        for (PostMarkdownVO postMarkdownVo : postMarkdownList) {
            StringBuilder content = new StringBuilder();
            Boolean needFrontMatter =
                Optional.ofNullable(postMarkdownParam.getNeedFrontMatter()).orElse(false);
            if (needFrontMatter) {
                // Add front-matter
                content.append(postMarkdownVo.getFrontMatter()).append("\n");
            }
            content.append(postMarkdownVo.getOriginalContent());
            try {
                String markdownFileName =
                    postMarkdownVo.getTitle() + "-" + postMarkdownVo.getSlug() + ".md";
                Path markdownFilePath = Paths.get(markdownFileTempPathName, markdownFileName);
                if (!Files.exists(markdownFilePath.getParent())) {
                    Files.createDirectories(markdownFilePath.getParent());
                }
                Path markdownDataPath = Files.createFile(markdownFilePath);
                FileUtils.writeStringToFile(markdownDataPath.toFile(), content.toString());
            } catch (IOException e) {
                throw new ServiceException("导出数据失败", e);
            }
        }

        // Create zip path
        String markdownZipFileName = HALO_BACKUP_MARKDOWN_PREFIX
            + DateTimeUtils.format(LocalDateTime.now(), HORIZONTAL_LINE_DATETIME_FORMATTER)
            + HaloUtils.simpleUUID().hashCode() + ".zip";

        // Create zip file
        Path markdownZipFilePath =
            Paths.get(haloProperties.getBackupMarkdownDir(), markdownZipFileName);
        if (!Files.exists(markdownZipFilePath.getParent())) {
            Files.createDirectories(markdownZipFilePath.getParent());
        }
        Path markdownZipPath = Files.createFile(markdownZipFilePath);
        // Zip file
        try (ZipOutputStream markdownZipOut = new ZipOutputStream(
            Files.newOutputStream(markdownZipPath))) {

            // Zip temporary directory
            Path markdownFileTempPath = Paths.get(markdownFileTempPathName);
            com.qinweizhao.site.utils.FileUtils.zip(markdownFileTempPath, markdownZipOut);

            // Zip upload sub-directory
            String uploadPathName =
                FileHandler.normalizeDirectory(haloProperties.getWorkDir()) + UPLOAD_SUB_DIR;
            Path uploadPath = Paths.get(uploadPathName);
            if (Files.exists(uploadPath)) {
                com.qinweizhao.site.utils.FileUtils.zip(uploadPath, markdownZipOut);
            }

            // Remove files in the temporary directory
            com.qinweizhao.site.utils.FileUtils.deleteFolder(markdownFileTempPath);

            // Build backup dto
            return buildBackupDto(DATA_EXPORT_MARKDOWN_BASE_URI, markdownZipPath);
        } catch (IOException e) {
            throw new ServiceException("Failed to export markdowns", e);
        }
    }

    @Override
    public List<BackupDTO> listMarkdowns() {
        // Ensure the parent folder exist
        Path backupParentPath = Paths.get(haloProperties.getBackupMarkdownDir());
        if (Files.notExists(backupParentPath)) {
            return Collections.emptyList();
        }

        // Build backup dto
        try (Stream<Path> subPathStream = Files.list(backupParentPath)) {
            return subPathStream
                .filter(backupPath -> StringUtils
                    .startsWithIgnoreCase(backupPath.getFileName().toString(),
                        HALO_BACKUP_MARKDOWN_PREFIX))
                .map(backupPath -> buildBackupDto(DATA_EXPORT_MARKDOWN_BASE_URI, backupPath))
                .sorted(Comparator.comparingLong(BackupDTO::getUpdateTime).reversed())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch backups", e);
        }
    }

    @Override
    public void deleteMarkdown(String filename) {
        Assert.hasText(filename, "File name must not be blank");

        Path backupRootPath = Paths.get(haloProperties.getBackupMarkdownDir());

        // Get backup path
        Path backupPath = backupRootPath.resolve(filename);

        // Check directory traversal
        checkDirectoryTraversal(backupRootPath, backupPath);

        try {
            // Delete backup file
            Files.delete(backupPath);
        } catch (NoSuchFileException e) {
            throw new NotFoundException("The file " + filename + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to delete backup", e);
        }
    }

    /**
     * Builds backup dto.
     *
     * @param backupPath backup path must not be null
     * @return backup dto
     */
    private BackupDTO buildBackupDto(@NonNull String basePath, @NonNull Path backupPath) {
        Assert.notNull(basePath, "Base path must not be null");
        Assert.notNull(backupPath, "Backup path must not be null");

        String backupFileName = backupPath.getFileName().toString();
        BackupDTO backup = new BackupDTO();
        try {
            backup.setDownloadLink(buildDownloadUrl(basePath, backupFileName));
            backup.setFilename(backupFileName);
            backup.setUpdateTime(Files.getLastModifiedTime(backupPath).toMillis());
            backup.setFileSize(Files.size(backupPath));
        } catch (IOException e) {
            throw new ServiceException("Failed to access file " + backupPath, e);
        }

        return backup;
    }

    /**
     * Builds download url.
     *
     * @param filename filename must not be blank
     * @return download url
     */
    @NonNull
    private String buildDownloadUrl(@NonNull String basePath, @NonNull String filename) {
        Assert.notNull(basePath, "Base path must not be null");
        Assert.hasText(filename, "File name must not be blank");

        // Composite http url
        String backupUri = basePath + HaloUtils.URL_SEPARATOR + filename;

        // Get a one-time token
        String oneTimeToken = oneTimeTokenService.create(backupUri);

        // Build full url
        return HaloUtils.compositeHttpUrl(optionService.getBlogBaseUrl(), backupUri)
            + "?"
            + HaloConst.ONE_TIME_TOKEN_QUERY_NAME
            + "=" + oneTimeToken;
    }

}
