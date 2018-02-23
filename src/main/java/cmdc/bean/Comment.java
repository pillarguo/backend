package cmdc.bean;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "comment")
public class Comment {
	@PathParam("commentId")
	private Integer commentId;
	@FormParam("commentUserId")
	private Integer commentUserId;
	@FormParam("postingId")
	private Integer postingId;
	@FormParam("createTime")
	private Date createTime;
	@FormParam("recipientNickname")
	private String recipientNickname;
	@FormParam("recipientCreateTime")
	private Date recipientCreateTime;
	@FormParam("floor")
	private Integer floor;
	@FormParam("displayStatus")
	private String displayStatus;
	@FormParam("commentContent")
	private String commentContent;

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(Integer commentUserId) {
		this.commentUserId = commentUserId;
	}

	public Integer getPostingId() {
		return postingId;
	}

	public void setPostingId(Integer postingId) {
		this.postingId = postingId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRecipientNickname() {
		return recipientNickname;
	}

	public void setRecipientNickname(String recipientNickname) {
		this.recipientNickname = recipientNickname;
	}

	public Date getRecipientCreateTime() {
		return recipientCreateTime;
	}

	public void setRecipientCreateTime(Date recipientCreateTime) {
		this.recipientCreateTime = recipientCreateTime;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getRecipientContent() {
		return recipientContent;
	}

	public void setRecipientContent(String recipientContent) {
		this.recipientContent = recipientContent;
	}

	public String getReserveField1() {
		return reserveField1;
	}

	public void setReserveField1(String reserveField1) {
		this.reserveField1 = reserveField1;
	}

	public String getReserveField2() {
		return reserveField2;
	}

	public void setReserveField2(String reserveField2) {
		this.reserveField2 = reserveField2;
	}

	public String getReserveField3() {
		return reserveField3;
	}

	public void setReserveField3(String reserveField3) {
		this.reserveField3 = reserveField3;
	}

	public String getReserveField4() {
		return reserveField4;
	}

	public void setReserveField4(String reserveField4) {
		this.reserveField4 = reserveField4;
	}

	public String getReserveField5() {
		return reserveField5;
	}

	public void setReserveField5(String reserveField5) {
		this.reserveField5 = reserveField5;
	}

	@FormParam("recipientContent")
	private String recipientContent;
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

}
