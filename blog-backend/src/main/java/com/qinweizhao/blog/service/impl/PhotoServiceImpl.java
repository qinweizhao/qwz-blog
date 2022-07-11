package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.PhotoMapper;
import com.qinweizhao.blog.model.entity.Photo;
import com.qinweizhao.blog.service.PhotoService;
import org.springframework.stereotype.Service;

/**
 * PhotoService implementation class
 *
 * @author ryanwang
 * @date 2019-03-14
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    //
//    @Override
//    public List<PhotoDTO> listDtos(Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        return listAll(sort).stream().map(photo -> (PhotoDTO) new PhotoDTO().convertFrom(photo)).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<PhotoTeamVO> listTeamVos(Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        // List all photos
//        List<PhotoDTO> photos = listDtos(sort);
//
//        // Get teams
//        Set<String> teams = ServiceUtils.fetchProperty(photos, PhotoDTO::getTeam);
//
//        Map<String, List<PhotoDTO>> teamPhotoListMap = ServiceUtils.convertToListMap(teams, photos, PhotoDTO::getTeam);
//
//        List<PhotoTeamVO> result = new LinkedList<>();
//
//        // Wrap photo team vo list
//        teamPhotoListMap.forEach((team, photoList) -> {
//            // Build photo team vo
//            PhotoTeamVO photoTeamVO = new PhotoTeamVO();
//            photoTeamVO.setTeam(team);
//            photoTeamVO.setPhotos(photoList);
//
//            // Add it to result
//            result.add(photoTeamVO);
//        });
//
//        return result;
//    }
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
//    @Override
//    public Photo createBy(PhotoParam photoParam) {
//        Assert.notNull(photoParam, "Photo param must not be null");
//
//        return create(photoParam.convertTo());
//    }
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
