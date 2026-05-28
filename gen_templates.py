import os

base = r"D:\java\项目\sjk-project\src\main\resources\templates"

def write_file(rel_path, content):
    path = os.path.join(base, rel_path)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"OK: {rel_path}")

# Common page wrapper
def page(title, body):
    return f"""<!DOCTYPE html>
<html lang="zh-CN" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{{layout :: common_head}}"><title>{title}</title></head>
<body>
<div th:insert="~{{layout :: sidebar}}"></div>
<div class="main">
<div th:replace="~{{layout :: alerts}}"></div>
{body}
</div>
<div th:replace="~{{layout :: scripts}}"></div>
</body></html>"""

# index.html
write_file("index.html", page("企业工资管理系统", """
<div class="page-header"><h2>企业工资管理系统</h2></div>
<div class="dashboard-grid">
<div class="card stat-card"><div class="label">员工总数</div><div class="value">12</div></div>
<div class="card stat-card"><div class="label">部门数量</div><div class="value">5</div></div>
<div class="card stat-card"><div class="label">工种数量</div><div class="value">10</div></div>
<div class="card stat-card"><div class="label">当前月份</div><div class="value" th:text="${{#temporals.format(#temporals.createNow(), 'yyyy-MM')}}">2026-05</div></div>
</div>
<div class="card"><div class="card-header">快捷操作</div><div class="card-body"><div class="row g-3">
<div class="col-md-3"><a href="/employee" class="btn btn-outline-primary w-100">员工信息管理</a></div>
<div class="col-md-3"><a href="/attendance" class="btn btn-outline-primary w-100">考勤登记</a></div>
<div class="col-md-3"><a href="/salary/generate" class="btn btn-outline-primary w-100">生成月工资</a></div>
<div class="col-md-3"><a href="/salary/stats" class="btn btn-outline-primary w-100">工资统计查询</a></div>
</div></div></div>
<div class="footer">企业工资管理系统 &copy; 2026</div>
"""))

# employee/list.html
write_file("employee/list.html", page("员工信息管理", """
<div class="page-header"><h2>员工信息管理</h2></div>
<div class="card">
<div class="card-header d-flex justify-content-between align-items-center"><span>员工列表</span><a href="/employee/add" class="btn btn-primary btn-sm">+ 新增员工</a></div>
<div class="card-body">
<form class="search-bar" method="get">
<input type="text" name="keyword" class="form-control" placeholder="员工ID或姓名" th:value="${{keyword}}" style="width:200px">
<button type="submit" class="btn btn-primary">查询</button><a href="/employee" class="btn btn-outline-secondary">重置</a>
</form>
<table class="table table-hover"><thead><tr><th>员工ID</th><th>姓名</th><th>部门</th><th>工种ID</th><th>入职日期</th><th>电话</th><th>状态</th><th>操作</th></tr></thead>
<tbody><tr th:each="emp : ${{employees}}">
<td th:text="${{emp.empId}}"></td><td th:text="${{emp.empName}}"></td><td th:text="${{emp.department}}"></td>
<td th:text="${{emp.jobId}}"></td><td th:text="${{#temporals.format(emp.hireDate, 'yyyy-MM-dd')}}"></td>
<td th:text="${{emp.phone}}"></td>
<td><span class="tag" th:classappend="${{emp.status == '在职'}} ? 'tag-on' : 'tag-off'" th:text="${{emp.status}}"></span></td>
<td><a th:href="@{{/employee/edit/{{id}}(id=${{emp.empId}})}}" class="btn btn-outline-secondary btn-sm">编辑</a>
<a th:href="@{{/employee/delete/{{id}}(id=${{emp.empId}})}}" class="btn btn-outline-danger btn-sm" onclick="return confirm('确认删除？')">删除</a></td>
</tr></tbody></table>
<div th:if="${{#lists.isEmpty(employees)}}" class="empty-state">暂无数据</div>
</div></div>
"""))

# employee/form.html
write_file("employee/form.html", page("编辑员工", """
<div class="page-header"><h2 th:text="${{employee.empId == null}} ? '新增员工' : '编辑员工'">编辑员工</h2></div>
<div class="card"><div class="card-body">
<form method="post" action="/employee/save" style="max-width:600px">
<div class="row g-3">
<div class="col-md-6"><label class="form-label">员工ID</label><input type="text" name="empId" class="form-control" th:value="${{employee.empId}} ?: ${{newId}}" th:readonly="${{employee.empId != null}}" required></div>
<div class="col-md-6"><label class="form-label">姓名</label><input type="text" name="empName" class="form-control" th:value="${{employee.empName}}" maxlength="10" required></div>
<div class="col-md-6"><label class="form-label">部门</label>
<select name="department" class="form-select" required>
<option value="">请选择</option>
<option value="行政部" th:selected="${{employee.department == '行政部'}}">行政部</option>
<option value="技术部" th:selected="${{employee.department == '技术部'}}">技术部</option>
<option value="生产部" th:selected="${{employee.department == '生产部'}}">生产部</option>
<option value="财务部" th:selected="${{employee.department == '财务部'}}">财务部</option>
<option value="人事部" th:selected="${{employee.department == '人事部'}}">人事部</option>
</select></div>
<div class="col-md-6"><label class="form-label">工种</label>
<select name="jobId" class="form-select" required>
<option value="">请选择</option>
<option th:each="job : ${{jobs}}" th:value="${{job.jobId}}" th:text="${{job.jobName + ' - ' + job.jobLevel}}" th:selected="${{employee.jobId == job.jobId}}"></option>
</select></div>
<div class="col-md-6"><label class="form-label">入职日期</label><input type="date" name="hireDate" class="form-control" th:value="${{employee.hireDate}}" required></div>
<div class="col-md-6"><label class="form-label">电话</label><input type="text" name="phone" class="form-control" th:value="${{employee.phone}}" maxlength="11"></div>
<div class="col-md-6"><label class="form-label">在职状态</label>
<select name="status" class="form-select"><option value="在职" th:selected="${{employee.status == null or employee.status == '在职'}}">在职</option><option value="离职" th:selected="${{employee.status == '离职'}}">离职</option></select></div>
</div>
<div class="mt-4"><button type="submit" class="btn btn-primary">保存</button><a href="/employee" class="btn btn-outline-secondary ms-2">取消</a></div>
</form></div></div>
"""))

print("Batch 1 done")
