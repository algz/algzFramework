<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<!-- 确保适当的绘制和触屏缩放 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bootstrap3</title>
<!-- jquery.js核心 JavaScript 文件(必须首个引用) -->
<script src="//code.jquery.com/jquery-1.10.1.min.js"></script>

<!-- Font Awesome图标字体 -->
<link rel="stylesheet" href="${basePath}/platform/common/font-Awesome/css/font-awesome.min.css">

<!-- 最新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="/algz-ui/platform/common/bootstrap3/css/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="/algz-ui/platform/common/bootstrap3/css/bootstrap-theme.min.css">
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/algz-ui/platform/common/bootstrap3/js/bootstrap.min.js"></script>

<!-- ZTree 核心 JavaScript 文件 -->
<link rel="stylesheet" href="/algz-ui/platform/common/ZTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/algz-ui/platform/common/ZTree/js/jquery.ztree.core-3.5.js"></script>

<!-- 自定义全局附加样式 -->
<link rel="stylesheet" href="/algz-ui/platform/common/css/main.css">

<style type="text/css">

</style>
<SCRIPT LANGUAGE="JavaScript">
   $(document).ready(function(){
	   $(".alert").alert()
   });
  </SCRIPT>
</head>
<body>
    <!-- .navbar-fixed-top可以让导航条固定在顶部。 -->
    <!-- .navbar-inverse类可以改变导航条的外观,反色的导航条. -->
	<nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
	    <!-- 标志位显示 -->
		<div class="navbar-header">
			<a class="navbar-brand" href="#">ALGZ框架</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse">
		    <!-- .navbar-left或者.navbar-right工具类给导航链接、表单、按钮或文本对齐。 -->
			<ul class="nav navbar-nav navbar-right">
				<li class="active"><a href="#">链接</a></li>
				<li class="dropdown">
				  <a class="dropdown-toggle" data-toggle="dropdown" href="#">简体中文 <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#">Action</a></li>
						<li><a href="#">Another action</a></li>
						<li><a href="#">Something else here</a></li>
						<li class="divider"></li>
						<li><a href="#">Separated link</a></li>
						<li class="divider"></li>
						<li><a href="#">One more separated link</a></li>
					</ul></li>
				<li class="dropdown">
				    <a class="dropdown-toggle" data-toggle="dropdown" href="#">欢迎， admin <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#">Change Password</a></li>
					</ul>
				</li>
				<li><a href="#">注销</a></li>
			</ul>
			<form class="navbar-form navbar-left" role="search">
				<div class="form-group">
					<input type="text" class="form-control" placeholder="Search">
				</div>
				<button type="submit" class="btn btn-default">Submit</button>
			</form>
		</div><!-- /.navbar-collapse -->
	</nav>

	<div class="row" style="margin-top: 65px;">
		<div class='col-md-2'>

		</div>
		<div class='col-md-5 '>
		<div class='panel panel-default'>
		<div class='panel-body'>
		<div class="alert alert-success">alert-success</div>
<div class="alert alert-info">alert-info</div>
<div class="alert alert-warning">alert-warning</div>
<div class="alert alert-danger">alert-danger</div>		
<div class="alert alert-warning alert-dismissable">
  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
  <strong>Warning!</strong> Best check yo self, you're not looking too good.
</div></div></div>
		</div>

	</div>
</body>
</html>
