package club.zhcs.cmdb.domain.acl;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import club.zhcs.titans.utils.db.po.Entity;

/**
 * 
 * @author kerbores(kerbores@gmail.com)
 *
 */
@Table("t_permission")
@Comment("权限表")
public class Permission extends Entity {

	@Column("p_name")
	@Name
	@Comment("权限名称")
	private String name;

	@Column("p_desc")
	@Comment("描述")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}