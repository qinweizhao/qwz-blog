package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.LinkMapper;
import com.qinweizhao.blog.model.entity.Link;
import com.qinweizhao.blog.service.LinkService;
import org.springframework.stereotype.Service;

/**
 * LinkService implementation class
 *
 * @author ryanwang
 * @date 2019-03-14
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {


//
//    @Override
//    public @NotNull List<LinkDTO> listDtos(@NotNull Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        return convertTo(listAll(sort));
//    }
//
//    @Override
//    public @NotNull List<LinkTeamVO> listTeamVos(@NotNull Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        // List all links
//        List<LinkDTO> links = listDtos(sort);
//
//        // Get teams
//        Set<String> teams = ServiceUtils.fetchProperty(links, LinkDTO::getTeam);
//
//        // Convert to team link list map (Key: team, value: link list)
//        Map<String, List<LinkDTO>> teamLinkListMap = ServiceUtils.convertToListMap(teams, links, LinkDTO::getTeam);
//
//        List<LinkTeamVO> result = new LinkedList<>();
//
//        // Wrap link team vo list
//        teamLinkListMap.forEach((team, linkList) -> {
//            // Build link team vo
//            LinkTeamVO linkTeamVO = new LinkTeamVO();
//            linkTeamVO.setTeam(team);
//            linkTeamVO.setLinks(linkList);
//
//            // Add it to result
//            result.add(linkTeamVO);
//        });
//
//        return result;
//    }
//
//    @Override
//    public @NotNull List<LinkTeamVO> listTeamVosByRandom(@NotNull Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//        List<LinkDTO> links = listDtos(sort);
//        Set<String> teams = ServiceUtils.fetchProperty(links, LinkDTO::getTeam);
//        Map<String, List<LinkDTO>> teamLinkListMap = ServiceUtils.convertToListMap(teams, links, LinkDTO::getTeam);
//        List<LinkTeamVO> result = new LinkedList<>();
//        teamLinkListMap.forEach((team, linkList) -> {
//            LinkTeamVO linkTeamVO = new LinkTeamVO();
//            linkTeamVO.setTeam(team);
//            Collections.shuffle(linkList);
//            linkTeamVO.setLinks(linkList);
//            result.add(linkTeamVO);
//        });
//        return result;
//    }
//
//    @Override
//    public @NotNull Link createBy(@NotNull LinkParam linkParam) {
//        Assert.notNull(linkParam, "Link param must not be null");
//
//        // Check the name
//        boolean exist = existByName(linkParam.getName());
//
//        if (exist) {
//            throw new AlreadyExistsException("友情链接 " + linkParam.getName() + " 已存在").setErrorData(linkParam.getName());
//        }
//
//        // Check the url
//        exist = existByUrl(linkParam.getUrl());
//
//        if (exist) {
//            throw new AlreadyExistsException("友情链接 " + linkParam.getUrl() + " 已存在").setErrorData(linkParam.getUrl());
//        }
//
//        return create(linkParam.convertTo());
//    }
//
//    @Override
//    public @NotNull Link updateBy(Integer id, @NotNull LinkParam linkParam) {
//        Assert.notNull(id, "Id must not be null");
//        Assert.notNull(linkParam, "Link param must not be null");
//
//        // Check the name
//        boolean exist = linkRepository.existsByNameAndIdNot(linkParam.getName(), id);
//        if (exist) {
//            throw new AlreadyExistsException("友情链接 " + linkParam.getName() + " 已存在").setErrorData(linkParam.getName());
//        }
//
//        // Check the url
//        exist = linkRepository.existsByUrlAndIdNot(linkParam.getUrl(), id);
//        if (exist) {
//            throw new AlreadyExistsException("友情链接 " + linkParam.getUrl() + " 已存在").setErrorData(linkParam.getUrl());
//        }
//
//        Link link = getById(id);
//        linkParam.update(link);
//
//        return update(link);
//    }
//
//    @Override
//    public boolean existByName(String name) {
//        Assert.hasText(name, "Link name must not be blank");
//        Link link = new Link();
//        link.setName(name);
//
//        return linkRepository.exists(Example.of(link));
//    }
//
//    @Override
//    public boolean existByUrl(String url) {
//        Assert.hasText(url, "Link url must not be blank");
//        Link link = new Link();
//        link.setUrl(url);
//
//        return linkRepository.exists(Example.of(link));
//    }
//
//    @Override
//    public List<String> listAllTeams() {
//        return linkRepository.findAllTeams();
//    }
//
//    @Override
//    public @NotNull List<Link> listAllByRandom() {
//        List<Link> allLink = linkRepository.findAll();
//        Collections.shuffle(allLink);
//        return allLink;
//    }
//
//    @NonNull
//    private List<LinkDTO> convertTo(@Nullable List<Link> links) {
//        if (CollectionUtils.isEmpty(links)) {
//            return Collections.emptyList();
//        }
//
//        return links.stream().map(link -> (LinkDTO) new LinkDTO().convertFrom(link))
//                .collect(Collectors.toList());
//    }
}
