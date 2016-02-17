<%@ page contentType="text/html;charset=UTF-8"   session="false" %>
<script type="text/javascript">
function back(url) {
	if( typeof url === 'undefined') {
		url = document.referrer;
	}
	location.replace(url);
}
</script>