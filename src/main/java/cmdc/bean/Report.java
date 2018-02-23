package cmdc.bean;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "report")
public class Report {
	@PathParam("reportId")
    private Integer reportId;
	@FormParam("reportUserId")
    private Integer reportUserId;
	@FormParam("postingId")
    private Integer postingId;
	@FormParam("commentId")
    private Integer commentId;
	@FormParam("createTime")
    private Date createTime;
	@FormParam("suspectUserId")
    private Integer suspectUserId;
	@FormParam("replyContent")
    private String replyContent;
	@FormParam("displayStatus")
    private String displayStatus;
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

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }



    public Integer getReportUserId() {
		return reportUserId;
	}

	public void setReportUserId(Integer reportUserId) {
		this.reportUserId = reportUserId;
	}

	public Integer getPostingId() {
        return postingId;
    }

    public void setPostingId(Integer postingId) {
        this.postingId = postingId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSuspectUserId() {
        return suspectUserId;
    }

    public void setSuspectUserId(Integer suspectUserId) {
        this.suspectUserId = suspectUserId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus == null ? null : displayStatus.trim();
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
}