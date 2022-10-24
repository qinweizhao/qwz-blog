<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
    <url>
        <loc>${blog_url!}</loc>
        <lastmod>${.now?string('yyyy-MM-dd')}</lastmod>
    </url>
    <#if posts?? && posts?size gt 0>
        <#list posts as post>
            <url>
                <loc>${post.fullPath!}</loc>
                <lastmod>${post.createTime}</lastmod>
            </url>
        </#list>
    </#if>
    <@categoryTag method="list">
        <#if categories?? && categories?size gt 0>
            <#list categories as category>
                <url>
                    <loc>${category.fullPath!}</loc>
                    <lastmod>${category.createTime}</lastmod>
                </url>
            </#list>
        </#if>
    </@categoryTag>
    <@tagTag method="list">
        <#if tags?? && tags?size gt 0>
            <#list tags as tag>
                <url>
                    <loc>${tag.fullPath!}</loc>
                    <lastmod>${tag.createTime}</lastmod>
                </url>
            </#list>
        </#if>
    </@tagTag>
</urlset>
