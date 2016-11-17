package cmcc.mobile.maintain.entity;

import java.util.Date;

public class Dept {
    private Long id;

    private Date createDate;

    private String createUser;

    private String depName;

    private Long parentId;

    private String bossareacode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName == null ? null : depName.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getBossareacode() {
        return bossareacode;
    }

    public void setBossareacode(String bossareacode) {
        this.bossareacode = bossareacode == null ? null : bossareacode.trim();
    }
}