package com.qinweizhao.site.model.entity;

import com.qinweizhao.site.model.enums.AttachmentType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * 附件
 *
 * @author ryanwang
 * @date 2019-03-12
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "attachments", indexes = {
        @Index(name = "attachments_media_type", columnList = "media_type"),
        @Index(name = "attachments_create_time", columnList = "create_time")})
@ToString
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.qinweizhao.site.model.entity.support.CustomIdGenerator")
    private Integer id;

    /**
     * 名字
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 路径
     */
    @Column(name = "path", length = 1023, nullable = false)
    private String path;

    /**
     * 文件 key :oss 文件或本地文件key 只用于删除
     */
    @Column(name = "file_key", length = 2047)
    private String fileKey;

    /**
     * 缩略图 路径
     */
    @Column(name = "thumb_path", length = 1023)
    private String thumbPath;

    /**
     * 类型
     */
    @Column(name = "media_type", length = 127, nullable = false)
    private String mediaType;

    /**
     * 后缀（png, zip, mp4, jpg）
     */
    @Column(name = "suffix", length = 50)
    private String suffix;

    /**
     * 宽度
     */
    @Column(name = "width")
    @ColumnDefault("0")
    private Integer width;

    /**
     * 高度
     */
    @Column(name = "height")
    @ColumnDefault("0")
    private Integer height;

    /**
     * 大小
     */
    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * 上传类型（本地，云）
     */
    @Column(name = "type")
    @ColumnDefault("0")
    private AttachmentType type;

    @Override
    public void prePersist() {
        super.prePersist();

        if (fileKey == null) {
            fileKey = "";
        }

        if (thumbPath == null) {
            thumbPath = "";
        }

        if (suffix == null) {
            suffix = "";
        }

        if (width == null) {
            width = 0;
        }

        if (height == null) {
            height = 0;
        }

        if (type == null) {
            type = AttachmentType.LOCAL;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Attachment that = (Attachment) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
