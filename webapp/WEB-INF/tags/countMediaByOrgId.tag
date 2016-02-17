<%@ attribute name="orgId" type="java.lang.String" required="true"%>
<%=com.lczy.common.util.SpringUtils.getBean(com.lczy.media.repositories.MediaDao.class).findByOrganizationId(orgId).size() %>