package cn.alvasw.web.aspect;

import cn.alvasw.commons.PageUtil;
import cn.alvasw.entity.PageResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
	public Object pageAround(ProceedingJoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String pageNum  = request.getParameter(localPageNum);
		String pageSize = request.getParameter(localPageSize);

		if (StringUtils.hasLength(pageNum) && StringUtils.hasLength(pageSize)) {
			PageUtil.setPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}

		try {
			Object o = joinPoint.proceed();
			if (o instanceof PageResult) {
				((PageResult) o).setPage(PageUtil.getPage());
			}
			return o;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			PageUtil.clear();
		}

		return null;
	}
}
