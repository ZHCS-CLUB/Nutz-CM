package club.zhcs.cm.rest.module;

import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.annotation.SessionBy;
import org.nutz.mvc.annotation.UrlMappingBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.plugins.apidoc.ApidocUrlMapping;
import org.nutz.plugins.apidoc.annotation.Api;
import org.nutz.plugins.apidoc.annotation.ApiMatchMode;

import club.zhcs.titans.nutz.captcha.JPEGView;
import club.zhcs.titans.nutz.module.base.AbstractBaseModule;
import club.zhcs.titans.utils.db.Result;

/**
 * @author kerbores(kerbores@gmail.com)
 *
 */
@Ok("json")
@Fail("http:500")
@Modules(scanPackage = true)
@SessionBy(ShiroSessionProvider.class)
@UrlMappingBy(ApidocUrlMapping.class)
@Api(name = "Nutz Config Management", description = "Nutz Config Management Rest Api", match = ApiMatchMode.ALL)
@IocBy(type = ComboIocProvider.class, args = { "*anno", "club.zhcs", "*tx", "*js", "ioc", "*async" })
public class MainModule extends AbstractBaseModule {

	@At("/")
	public Result index() {
		return Result.success();
	}

	@At
	public View captcha(@Param(value = "length", df = "5") int length) {
		return new JPEGView(null, length);
	}

}
