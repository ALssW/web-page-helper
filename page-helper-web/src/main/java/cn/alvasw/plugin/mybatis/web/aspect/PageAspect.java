package cn.alvasw.plugin.mybatis.web.aspect;

import cn.alvasw.plugin.mybatis.commons.PageUtil;
import cn.alvasw.plugin.mybatis.entity.PageResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
@Aspect
public class PageAspect {

	@Value("pageNum")
	private String localPageNum;
	@Value("pageSize")
	private String localPageSize;

	@Around("@within(org.springframework.web.bind.annotation.RestController)")
	public Object pageAround(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String pageNum  = request.getParameter(localPageNum);
		String pageSize = request.getParameter(localPageSize);

		if (StringUtils.hasLength(pageNum) && StringUtils.hasLength(pageSize)) {
			int num  = Integer.parseInt(pageNum);
			int size = Integer.parseInt(pageSize);
			if (num <= 0 || size <= 0) {
				throw new InvalidParameterException("page num or page size cant small than 1");
			}
			PageUtil.setPage(num, size);
		}

		try {
			Object o = joinPoint.proceed();
			if (o instanceof PageResult) {
				((PageResult) o).setPage(PageUtil.getPage());
			}
			return o;
		} finally {
			PageUtil.clear();
		}
	}
}
