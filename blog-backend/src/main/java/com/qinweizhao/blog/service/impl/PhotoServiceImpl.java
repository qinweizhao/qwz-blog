package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.convert.PhotoConvert;
import com.qinweizhao.blog.mapper.PhotoMapper;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PhotoDTO;
import com.qinweizhao.blog.model.entity.Photo;
import com.qinweizhao.blog.model.params.PhotoQueryParam;
import com.qinweizhao.blog.model.params.PhotoParam;
import com.qinweizhao.blog.model.vo.PhotoTeamVO;
import com.qinweizhao.blog.service.PhotoService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PhotoService implementation class
 *
 * @author qinweizhao
 * @date 2019-03-14
 */
@Service
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoMapper photoMapper;

    @Override
    public List<PhotoDTO> list() {
        List<Photo> photos = photoMapper.selectList(Wrappers.emptyWrapper());
        return PhotoConvert.INSTANCE.convertToDTO(photos);
    }

    @Override
    public PageResult<PhotoDTO> page(PhotoQueryParam param) {
        PageResult<Photo> result = photoMapper.selectPagePhotos(param);
        return PhotoConvert.INSTANCE.convert(result);
    }

    @Override
    public PhotoDTO getById(Integer photoId) {
        Photo photo = photoMapper.selectById(photoId);
        return PhotoConvert.INSTANCE.convert(photo);
    }

    @Override
    public boolean removeById(Integer photoId) {
        return photoMapper.deleteById(photoId) > 0;
    }

    @Override
    public boolean save(PhotoParam photoParam) {
        Photo photo = PhotoConvert.INSTANCE.convert(photoParam);
        return photoMapper.insert(photo) > 0;
    }

    @Override
    public boolean updateById(Integer photoId, PhotoParam photoParam) {
        Photo photo = PhotoConvert.INSTANCE.convert(photoParam);
        photo.setId(photoId);
        return photoMapper.updateById(photo) > 0;
    }

    @Override
    public List<String> listTeams() {
        return photoMapper.selectListTeam();
    }

    @Override
    public Long count() {
        return photoMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public List<PhotoDTO> listByTeam(String team) {
        List<Photo> photos = photoMapper.selectListByTeam(team);
        return PhotoConvert.INSTANCE.convertToDTO(photos);
    }

    @Override
    public List<PhotoTeamVO> listPhotoTeam() {

        List<PhotoDTO> photos = this.list();

        Set<String> teams = ServiceUtils.fetchProperty(photos, PhotoDTO::getTeam);

        Map<String, List<PhotoDTO>> teamPhotoListMap = ServiceUtils.convertToListMap(teams, photos, PhotoDTO::getTeam);

        List<PhotoTeamVO> result = new LinkedList<>();

        teamPhotoListMap.forEach((team, photoList) -> {

            PhotoTeamVO photoTeamVO = new PhotoTeamVO();
            photoTeamVO.setTeam(team);
            photoTeamVO.setPhotos(photoList);

            result.add(photoTeamVO);
        });

        return result;
    }



//
//    @Override
//    public List<PhotoDTO> listByTeam(String team, Sort sort) {
//        List<Photo> photos = this.baseMapper.selectByTeam(team, sort);
//        return PhotoConvert.INSTANCE.convertToDTO(photos);
//    }
//
//
//    @Override
//    public Page<PhotoDTO> pageDtosBy(Pageable pageable, PhotoQuery photoQuery) {
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // List all
//        Page<Photo> photoPage = photoRepository.findAll(buildSpecByQuery(photoQuery), pageable);
//
//        // Convert and return
//        return photoPage.map(photo -> new PhotoDTO().convertFrom(photo));
//    }
//
//
//    @Override
//    public List<String> listAllTeams() {
//        return this.baseMapper.selectListTeam();
//    }
//
//    @Override
//    public List<PhotoDTO> replaceUrl(String oldUrl, String newUrl) {
//        List<Photo> photos = listAll();
//        List<Photo> replaced = new ArrayList<>();
//        photos.forEach(photo -> {
//            if (StringUtils.isNotEmpty(photo.getThumbnail())) {
//                photo.setThumbnail(photo.getThumbnail().replace(oldUrl, newUrl));
//            }
//            if (StringUtils.isNotEmpty(photo.getUrl())) {
//                photo.setUrl(photo.getUrl().replaceAll(oldUrl, newUrl));
//            }
//            replaced.add(photo);
//        });
//        List<Photo> updated = updateInBatch(replaced);
//        return updated.stream().map(photo -> (PhotoDTO) new PhotoDTO().convertFrom(photo)).collect(Collectors.toList());
//    }
//
//    @NonNull
//    private Specification<Photo> buildSpecByQuery(@NonNull PhotoQuery photoQuery) {
//        Assert.notNull(photoQuery, "Photo query must not be null");
//
//        return (Specification<Photo>) (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new LinkedList<>();
//
//            if (photoQuery.getTeam() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("team"), photoQuery.getTeam()));
//            }
//
//            if (photoQuery.getKeyword() != null) {
//
//                String likeCondition = String.format("%%%s%%", StringUtils.strip(photoQuery.getKeyword()));
//
//                Predicate nameLike = criteriaBuilder.like(root.get("name"), likeCondition);
//                Predicate descriptionLike = criteriaBuilder.like(root.get("description"), likeCondition);
//                Predicate locationLike = criteriaBuilder.like(root.get("location"), likeCondition);
//
//                predicates.add(criteriaBuilder.or(nameLike, descriptionLike, locationLike));
//            }
//
//            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
//        };
//    }
}
