package club.zhcs.cmdb.domain.acl;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Times;

import club.zhcs.titans.utils.db.po.Entity;

/**
 * 
 * @author 王贵源
 *
 * @email kerbores@kerbores.com
 *
 * @description 用户实体类
 * 
 * @copyright 内部代码,禁止转发
 *
 *
 * @time 2016年1月26日 下午2:18:45
 */
@Table("t_user")
@Comment("用户表")
public class User extends Entity {

	@Column("u_name")
	@Name
	@Comment("用户登录名")
	private String name;

	@Column("u_real_name")
	@Comment("用户真实姓名")
	private String realName;

	@Column("u_nick_name")
	@Comment("用户昵称")
	private String nickName;

	@Column("u_pwd")
	@Comment("用户登录密码")
	private String password;

	@Column("u_phone")
	@Comment("用户手机号")
	private String phone;

	@Column("u_email")
	@Comment("用户邮箱")
	private String email;

	@Column("u_head_key")
	@Comment("用户头像")
	@ColDefine(width = 200)
	private String headKey;

	private String headUrl;

	@Column("u_create_time")
	@Comment("用户创建时间")
	private Date createTime = Times.now();

	@Column("u_openid")
	@Comment("用户微信 openid")
	private String openid;

	@Column("u_locked")
	@Comment("锁定标识")
	private boolean locked;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public String getEmail() {
		return email;
	}

	public String getHeadKey() {
		return headKey;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	public String getOpenid() {
		return openid;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public String getRealName() {
		return realName;
	}

	public boolean isAvailable() {
		return !isLocked();
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setHeadKey(String headKey) {
		this.headKey = headKey;
	}

	protected void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}