<div id="contentwrapper">
	<div class="main_content">
		<#include "top.ftl">
		<div class="row-fluid">
			<div class="span12">
				<h3 class="heading">分享Happy</h3>
					<div class="alert alert-error">
						<a class="close" data-dismiss="alert">×</a>
						<strong>操作信息:
							欢迎
							<#if user??>
								${user.username}
							<#else>
								游客
							</#if>
						</strong>
					</div>
					<#if user??>
					<div class="btn-group sepH_b">
							<button data-toggle="dropdown" class="btn dropdown-toggle">
								行数 <span class="caret"></span>
							</button>
						<ul class="dropdown-menu">
							<li><a href="/user/pagenum/5">默认5行</a></li>
							<li><a href="/user/pagenum/10">每页10行</a></li>
							<li><a href="/user/pagenum/2">每页2行</a></li>
						</ul>
					</div>
					</#if>
				<table class="table table-bordered table-striped table_vam"
					id="dt_gal">
					<thead>
						<tr>
							<th class="table_checkbox">序号</th>
							<th style="width: 50px">发布人</th>
							<th style="width: 100px">主帖标题</th>
							<th style="width: 420px">主帖内容</th>
							<th style="width: 60px">发布日期</th>
							<th style="width: 60px">操作</th>
						</tr>
					</thead>
					<tbody>
						
                    	<#if pb??>
							<#list pb.data as d>
						
								<tr>

									<td>${d_index+1}</td>
									<td><a href="/upload/.jpg"
										
										title="" class="cbox_single thumbnail">
                                        <img src="/user/pic/${d.user.userid}"
											alt="" style="height: 50px; width: 50px" />

									</a>
									</td>
									<td>
									<a
										href="">${d.title}</a>
									</td>
									<td>${d.content}</td>
									<td>${d.datetime}</td>
									<td>
										<!-- 没登陆，游客 uid=0 -->
												
											<!-- 锚点传值 -->
											<#--<a href="" title="灌水" data-toggle="modal"-->
											<#--id="myp" data-backdrop="static"-->
											<#--onclick="rshow(${data.id},${uid},${data.user.id});">-->
												 <#--<i class="icon-eye-open"></i>-->
											<#---->
									<#--</a>-->
										<#assign uid = 0/>
										<#if user??>
										    <#assign uid = user.userid/>
										</#if>
                                        <a href="#rshow" title="灌水" data-toggle="modal"
                                           id="myp" data-backdrop="static"
                                           onclick="rshow(${d.id},${uid},${d.user.userid})">
                                            <i class="icon-eye-open"></i>
                                        </a>
											<!-- 是本人贴可以删除和修改 -->
										<#if user??>
											<#if user.userid == d.user.userid>
												<a
													href="/article/delZT/${d.id}" title="删除本帖">
													<i class="icon-trash"></i>
												</a>
											</#if>
										</#if>
									</td>
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
		
			<#include "page.ftl">
		

	</div>


</div>