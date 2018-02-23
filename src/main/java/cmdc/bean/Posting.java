package cmdc.bean;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;

public class Posting {
	@PathParam("postingId")
    private Integer postingId;
	@FormParam("userId")
    private Integer userId;
	@FormParam("postingTitle")
    private String postingTitle;
	@FormParam("createTime")
    private Date createTime;
	@FormParam("updateTime")
    private Date updateTime;
	@FormParam("displayStatus")
    private String displayStatus;
	@FormParam("readingTimes")
    private Integer readingTimes;
	@FormParam("commentTimes")
    private Integer commentTimes;
	//版块
	@FormParam("section")
    private String section;
	//加精
	@FormParam("essence")
    private String essence;
	//官方认证
	@FormParam("authentication")
    private String authentication;
	@FormParam("reserveField1")
    private String reserveField1;
	@FormParam("reserveField2")
    private String reserveField2;
	@FormParam("reserveField3")
    private String reserveField3;
	@FormParam("reserveField4")
    private String reserveField4;
	@FormParam("reserveField5")
    private String reserveField5;
	@FormParam("postingContent")
    private String postingContent;

    public Integer getPostingId() {
        return postingId;
    }

    public void setPostingId(Integer postingId) {
        this.postingId = postingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPostingTitle() {
        return postingTitle;
    }

    public void setPostingTitle(String postingTitle) {
        this.postingTitle = postingTitle == null ? null : postingTitle.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus == null ? null : displayStatus.trim();
    }

    public Integer getReadingTimes() {
        return readingTimes;
    }

    public void setReadingTimes(Integer readingTimes) {
        this.readingTimes = readingTimes;
    }

    public Integer getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(Integer commentTimes) {
        this.commentTimes = commentTimes;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section == null ? null : section.trim();
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence == null ? null : essence.trim();
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication == null ? null : authentication.trim();
    }

    public String getReserveField1() {
        return reserveField1;
    }

    public void setReserveField1(String reserveField1) {
        this.reserveField1 = reserveField1 == null ? null : reserveField1.trim();
    }

    public String getReserveField2() {
        return reserveField2;
    }

    public void setReserveField2(String reserveField2) {
        this.reserveField2 = reserveField2 == null ? null : reserveField2.trim();
    }

    public String getReserveField3() {
        return reserveField3;
    }

    public void setReserveField3(String reserveField3) {
        this.reserveField3 = reserveField3 == null ? null : reserveField3.trim();
    }

    public String getReserveField4() {
        return reserveField4;
    }

    public void setReserveField4(String reserveField4) {
        this.reserveField4 = reserveField4 == null ? null : reserveField4.trim();
    }

    public String getReserveField5() {
        return reserveField5;
    }

    public void setReserveField5(String reserveField5) {
        this.reserveField5 = reserveField5 == null ? null : reserveField5.trim();
    }

    public String getPostingContent() {
        return postingContent;
    }

    public void setPostingContent(String postingContent) {
        this.postingContent = postingContent == null ? null : postingContent.trim();
    }
}