// API helper
const $api = {
  async get(url, params) {
    if (params) { const q = new URLSearchParams(params).toString(); if (q) url += '?' + q; }
    const r = await fetch(url); if (!r.ok) throw new Error(await r.text()); return r.json();
  },
  async post(url, data) {
    const r = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
    if (!r.ok) throw new Error(await r.text()); return r.json();
  },
  async del(url) {
    const r = await fetch(url, { method: 'DELETE' });
    if (!r.ok) throw new Error(await r.text()); return r.json();
  }
};

const { createApp, ref, reactive, onMounted, computed, provide, inject } = Vue;
const { createRouter, createWebHashHistory } = VueRouter;

// ===== DASHBOARD =====
const Dashboard = {
  template: `
<div>
  <div class="page-header"><h2>企业工资管理系统</h2></div>
  <div class="dashboard-grid">
    <div class="card stat-card"><div class="label">员工总数</div><div class="value">{{stats.empCount}}</div></div>
    <div class="card stat-card"><div class="label">部门数量</div><div class="value">{{stats.deptCount}}</div></div>
    <div class="card stat-card"><div class="label">工种数量</div><div class="value">{{stats.jobCount}}</div></div>
    <div class="card stat-card"><div class="label">当前月份</div><div class="value">{{now}}</div></div>
  </div>
  <div class="card"><div class="card-header">快捷操作</div>
    <div class="card-body"><div class="row g-3">
      <div class="col-md-3"><router-link to="/employees" class="btn btn-outline-primary w-100">员工信息管理</router-link></div>
      <div class="col-md-3"><router-link to="/attendance" class="btn btn-outline-primary w-100">考勤登记</router-link></div>
      <div class="col-md-3"><router-link to="/salary/generate" class="btn btn-outline-primary w-100">生成月工资</router-link></div>
      <div class="col-md-3"><router-link to="/monthly-stats" class="btn btn-outline-primary w-100">工资统计查询</router-link></div>
    </div></div>
  </div>
  <div class="footer">企业工资管理系统 &copy; 2026</div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const stats = reactive({ empCount: 12, deptCount: 5, jobCount: 10 });
    const now = new Date().toISOString().substring(0, 7);
    onMounted(async () => {
      try { const emps = await $api.get('/api/employee'); stats.empCount = emps.length; } catch(e){}
      try { const jobs = await $api.get('/api/job'); stats.jobCount = jobs.length; } catch(e){}
    });
    return { stats, now };
  }
};

// ===== EMPLOYEE =====
const EmployeeList = {
  template: `
<div>
  <div class="page-header"><h2>员工信息管理</h2></div>
  <div class="card">
    <div class="card-header"><span>员工列表</span><button class="btn btn-primary btn-sm" @click="openAdd">+ 新增员工</button></div>
    <div class="card-body">
      <div class="search-bar">
        <input v-model="keyword" class="form-control" placeholder="员工ID或姓名" style="width:200px" @keyup.enter="search">
        <button class="btn btn-primary" @click="search">查询</button>
        <button class="btn btn-outline-secondary" @click="keyword='';search()">重置</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>员工ID</th><th>姓名</th><th>部门</th><th>工种ID</th><th>入职日期</th><th>电话</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="e in list" :key="e.empId">
            <td>{{e.empId}}</td><td>{{e.empName}}</td><td>{{e.department}}</td><td>{{e.jobId}}</td><td>{{e.hireDate}}</td><td>{{e.phone}}</td>
            <td><span :class="e.status==='在职'?'tag tag-on':'tag tag-off'">{{e.status}}</span></td>
            <td>
              <button class="btn btn-outline-secondary btn-sm" @click="openEdit(e)">编辑</button>
              <button class="btn btn-outline-danger btn-sm" @click="doDelete(e.empId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="list.length===0" class="empty-state">暂无数据</div>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop" @click.self="showModal=false">
    <div class="modal-box">
      <h5 class="mb-3">{{editing.empId?'编辑员工':'新增员工'}}</h5>
      <div class="row-g3">
        <div class="col-half"><label class="form-label">员工ID</label><input v-model="editing.empId" class="form-control" :readonly="!!editing.empId" required></div>
        <div class="col-half"><label class="form-label">姓名</label><input v-model="editing.empName" class="form-control" maxlength="10" required></div>
        <div class="col-half"><label class="form-label">部门</label><select v-model="editing.department" class="form-select" required><option value="">请选择</option><option>行政部</option><option>技术部</option><option>生产部</option><option>财务部</option><option>人事部</option></select></div>
        <div class="col-half"><label class="form-label">工种</label><select v-model="editing.jobId" class="form-select" required><option value="">请选择</option><option v-for="j in jobList" :key="j.jobId" :value="j.jobId">{{j.jobName}} - {{j.jobLevel}}</option></select></div>
        <div class="col-half"><label class="form-label">入职日期</label><input type="date" v-model="editing.hireDate" class="form-control" required></div>
        <div class="col-half"><label class="form-label">电话</label><input v-model="editing.phone" class="form-control" maxlength="11"></div>
        <div class="col-half"><label class="form-label">在职状态</label><select v-model="editing.status" class="form-select"><option value="在职">在职</option><option value="离职">离职</option></select></div>
      </div>
      <div class="mt-3"><button class="btn btn-primary" @click="doSave">保存</button><button class="btn btn-outline-secondary ms-2" @click="showModal=false">取消</button></div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), keyword = ref(''), showModal = ref(false);
    const editing = reactive({ empId:'', empName:'', department:'', jobId:'', hireDate:'', phone:'', status:'在职' });
    const jobList = ref([]);
    const search = async () => { try { list.value = await $api.get('/api/employee', keyword.value ? { keyword: keyword.value } : null); } catch(e) { showErr(e.message); } };
    const openAdd = async () => { try { const r = await $api.get('/api/employee/newId'); editing.empId = r.empId; jobList.value = await $api.get('/api/employee/jobs'); } catch(e){} editing.empName=''; editing.department=''; editing.jobId=''; editing.hireDate=''; editing.phone=''; editing.status='在职'; showModal.value = true; };
    const openEdit = async (e) => { Object.assign(editing, e); try { jobList.value = await $api.get('/api/employee/jobs'); } catch(e){} showModal.value = true; };
    const doSave = async () => { try { await $api.post('/api/employee', { ...editing }); showModal.value = false; showMsg('保存成功'); search(); } catch(e) { showErr(e.message); } };
    const doDelete = async (id) => { if (!confirm('确认删除？')) return; try { await $api.del('/api/employee/'+id); showMsg('删除成功'); search(); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, keyword, search, showModal, editing, jobList, openAdd, openEdit, doSave, doDelete };
  }
};

// ===== JOB =====
const JobList = {
  template: `
<div>
  <div class="page-header"><h2>工种信息管理</h2></div>
  <div class="card">
    <div class="card-header"><span>工种列表</span><button class="btn btn-primary btn-sm" @click="openAdd">+ 新增工种</button></div>
    <div class="card-body">
      <div class="search-bar">
        <input v-model="keyword" class="form-control" placeholder="工种名称或等级" style="width:200px" @keyup.enter="search">
        <button class="btn btn-primary" @click="search">查询</button>
        <button class="btn btn-outline-secondary" @click="keyword='';search()">重置</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>工种ID</th><th>名称</th><th>等级</th><th>基本工资</th><th>备注</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="j in list" :key="j.jobId">
            <td>{{j.jobId}}</td><td>{{j.jobName}}</td><td>{{j.jobLevel}}</td><td>{{j.baseSalary}}</td><td>{{j.remark}}</td>
            <td>
              <button class="btn btn-outline-secondary btn-sm" @click="openEdit(j)">编辑</button>
              <button class="btn btn-outline-danger btn-sm" @click="doDelete(j.jobId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop" @click.self="showModal=false">
    <div class="modal-box">
      <h5 class="mb-3">{{editing.jobId?'编辑工种':'新增工种'}}</h5>
      <div class="row-g3">
        <div class="col-half"><label class="form-label">工种ID</label><input v-model="editing.jobId" class="form-control" :readonly="!!editing.jobId" required></div>
        <div class="col-half"><label class="form-label">工种名称</label><input v-model="editing.jobName" class="form-control" maxlength="15" required></div>
        <div class="col-half"><label class="form-label">工种等级</label><select v-model="editing.jobLevel" class="form-select" required><option value="">请选择</option><option>初级</option><option>中级</option><option>高级</option></select></div>
        <div class="col-half"><label class="form-label">基本工资(元)</label><input type="number" step="0.01" v-model="editing.baseSalary" class="form-control" required></div>
        <div class="col-half"><label class="form-label">备注</label><input v-model="editing.remark" class="form-control" maxlength="100"></div>
      </div>
      <div class="mt-3"><button class="btn btn-primary" @click="doSave">保存</button><button class="btn btn-outline-secondary ms-2" @click="showModal=false">取消</button></div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), keyword = ref(''), showModal = ref(false);
    const editing = reactive({ jobId:'', jobName:'', jobLevel:'', baseSalary:'', remark:'' });
    const search = async () => { try { list.value = await $api.get('/api/job', keyword.value ? { keyword: keyword.value } : null); } catch(e) { showErr(e.message); } };
    const openAdd = async () => { try { const r = await $api.get('/api/job/newId'); editing.jobId = r.jobId; } catch(e){} editing.jobName=''; editing.jobLevel=''; editing.baseSalary=''; editing.remark=''; showModal.value = true; };
    const openEdit = async (j) => { Object.assign(editing, j); showModal.value = true; };
    const doSave = async () => { try { await $api.post('/api/job', { ...editing }); showModal.value = false; showMsg('保存成功'); search(); } catch(e) { showErr(e.message); } };
    const doDelete = async (id) => { if (!confirm('确认删除？')) return; try { await $api.del('/api/job/'+id); showMsg('删除成功'); search(); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, keyword, search, showModal, editing, openAdd, openEdit, doSave, doDelete };
  }
};
// ===== ATTENDANCE =====
const AttendanceList = {
  template: `
<div>
  <div class="page-header"><h2>考勤登记</h2></div>
  <div class="card">
    <div class="card-header"><span>考勤列表</span><button class="btn btn-primary btn-sm" @click="openAdd">+ 新增考勤</button></div>
    <div class="card-body">
      <div class="search-bar">
        <input v-model="fEmpId" class="form-control" placeholder="员工ID" style="width:130px">
        <select v-model="fDept" class="form-select" style="width:140px"><option value="">全部部门</option><option>行政部</option><option>技术部</option><option>生产部</option><option>财务部</option><option>人事部</option></select>
        <input type="month" v-model="fMonth" class="form-control" style="width:160px">
        <button class="btn btn-primary" @click="search">查询</button>
        <button class="btn btn-outline-secondary" @click="fEmpId='';fDept='';fMonth='';search()">重置</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>姓名</th><th>员工ID</th><th>月份</th><th>出勤天数</th><th>请假天数</th><th>加班时长(h)</th><th>加班天数</th><th>备注</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.attId">
            <td>{{a.empName}}</td><td>{{a.empId}}</td><td>{{a.attMonth}}</td><td>{{a.workDays}}</td><td>{{a.leaveDays}}</td><td>{{a.overtimeHours}}</td><td>{{a.overtimeDays}}</td><td style="max-width:120px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{a.attRemark}}</td>
            <td>
              <button class="btn btn-outline-secondary btn-sm" @click="openEdit(a)">编辑</button>
              <button class="btn btn-outline-danger btn-sm" @click="doDelete(a.attId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop" @click.self="showModal=false">
    <div class="modal-box">
      <h5 class="mb-3">{{editing.attId?'编辑考勤':'新增考勤'}}</h5>
      <div class="row-g3">
        <div class="col-half"><label class="form-label">考勤ID</label><input v-model="editing.attId" class="form-control" required readonly></div>
        <div class="col-half"><label class="form-label">员工</label><select v-model="editing.empId" class="form-select" required :disabled="!!editing.attId"><option value="">请选择</option><option v-for="e in empList" :key="e.empId" :value="e.empId">{{e.empName}} ({{e.empId}})</option></select></div>
        <div class="col-half"><label class="form-label">考勤月份</label><input type="month" v-model="editing.attMonth" class="form-control" required></div>
        <div class="col-half"><label class="form-label">正常出勤天数</label><input type="number" v-model="editing.workDays" class="form-control" min="0" max="31" required></div>
        <div class="col-half"><label class="form-label">请假天数</label><input type="number" step="0.5" v-model="editing.leaveDays" class="form-control" min="0"></div>
        <div class="col-half"><label class="form-label">加班时长(h)</label><input type="number" step="0.5" v-model="editing.overtimeHours" class="form-control" min="0"></div>
        <div class="col-half"><label class="form-label">加班天数</label><input type="number" step="0.5" v-model="editing.overtimeDays" class="form-control" min="0"></div>
        <div class="col-half"><label class="form-label">备注</label><input v-model="editing.attRemark" class="form-control" maxlength="100"></div>
      </div>
      <div class="mt-3"><button class="btn btn-primary" @click="doSave">保存</button><button class="btn btn-outline-secondary ms-2" @click="showModal=false">取消</button></div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), fEmpId = ref(''), fDept = ref(''), fMonth = ref(''), showModal = ref(false);
    const editing = reactive({ attId:'', empId:'', attMonth:'', workDays:'', leaveDays:'0', overtimeHours:'0', overtimeDays:'0', attRemark:'' });
    const empList = ref([]);
    const search = async () => { try { list.value = await $api.get('/api/attendance', { empId: fEmpId.value||undefined, department: fDept.value||undefined, attMonth: fMonth.value||undefined }); } catch(e) { showErr(e.message); } };
    const openAdd = async () => { try { empList.value = await $api.get('/api/attendance/employees'); } catch(e){} editing.attId = 'ATT-'+new Date().toISOString().substring(0,7)+'-'+Math.random().toString(36).substring(2,8).toUpperCase(); editing.empId=''; editing.attMonth=''; editing.workDays=''; editing.leaveDays='0'; editing.overtimeHours='0'; editing.overtimeDays='0'; editing.attRemark=''; showModal.value = true; };
    const openEdit = async (a) => { Object.assign(editing, a); try { empList.value = await $api.get('/api/attendance/employees'); } catch(e){} showModal.value = true; };
    const doSave = async () => { try { await $api.post('/api/attendance', { ...editing }); showModal.value = false; showMsg('保存成功'); search(); } catch(e) { showErr(e.message); } };
    const doDelete = async (id) => { if (!confirm('确认删除？')) return; try { await $api.del('/api/attendance/'+id); showMsg('删除成功'); search(); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, fEmpId, fDept, fMonth, search, showModal, editing, empList, openAdd, openEdit, doSave, doDelete };
  }
};

// ===== ALLOWANCE =====
const AllowanceList = {
  template: `
<div>
  <div class="page-header"><h2>加班津贴管理</h2></div>
  <div class="card">
    <div class="card-header"><span>津贴列表</span><button class="btn btn-primary btn-sm" @click="openAdd">+ 新增津贴</button></div>
    <div class="card-body">
      <div class="search-bar">
        <input v-model="fEmpId" class="form-control" placeholder="员工ID" style="width:130px">
        <select v-model="fType" class="form-select" style="width:160px"><option value="">全部类型</option><option>工作日加班</option><option>休息日加班</option><option>法定节假日加班</option></select>
        <input type="month" v-model="fMonth" class="form-control" style="width:160px">
        <button class="btn btn-primary" @click="search">查询</button>
        <button class="btn btn-outline-secondary" @click="fEmpId='';fType='';fMonth='';search()">重置</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>姓名</th><th>员工ID</th><th>加班日期</th><th>加班类型</th><th>时长/天数</th><th>津贴标准</th><th>津贴金额</th><th>月份</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.allowId">
            <td>{{a.empName}}</td><td>{{a.empId}}</td><td>{{a.overtimeDate}}</td><td>{{a.overtimeType}}</td><td>{{a.overtimeAmount}}</td><td>{{a.allowanceRate}}</td><td>{{a.allowanceAmount}}</td><td>{{a.attMonth}}</td>
            <td><button class="btn btn-outline-danger btn-sm" @click="doDelete(a.allowId)">删除</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop" @click.self="showModal=false">
    <div class="modal-box">
      <h5 class="mb-3">新增加班津贴</h5>
      <div class="row-g3">
        <div class="col-half"><label class="form-label">津贴ID</label><input v-model="editing.allowId" class="form-control" required></div>
        <div class="col-half"><label class="form-label">员工</label><select v-model="editing.empId" class="form-select" required><option value="">请选择</option><option v-for="e in empList" :key="e.empId" :value="e.empId">{{e.empName}} ({{e.empId}})</option></select></div>
        <div class="col-half"><label class="form-label">加班类型</label><select v-model="editing.overtimeType" class="form-select" required><option value="">请选择</option><option>工作日加班</option><option>休息日加班</option><option>法定节假日加班</option></select></div>
        <div class="col-half"><label class="form-label">加班时长/天数</label><input type="number" step="0.5" v-model="editing.overtimeAmount" class="form-control" min="0" required></div>
        <div class="col-half"><label class="form-label">津贴标准(元/单位)</label><input type="number" step="0.01" v-model="editing.allowanceRate" class="form-control" required></div>
        <div class="col-half"><label class="form-label">津贴金额(元)</label><input type="number" step="0.01" v-model="editing.allowanceAmount" class="form-control" required></div>
        <div class="col-half"><label class="form-label">加班日期</label><input type="date" v-model="editing.overtimeDate" class="form-control" required></div>
        <div class="col-half"><label class="form-label">考勤月份</label><input type="month" v-model="editing.attMonth" class="form-control" required></div>
      </div>
      <div class="mt-3"><button class="btn btn-primary" @click="doSave">保存</button><button class="btn btn-outline-secondary ms-2" @click="showModal=false">取消</button></div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), fEmpId = ref(''), fType = ref(''), fMonth = ref(''), showModal = ref(false);
    const editing = reactive({ allowId:'', empId:'', overtimeType:'', overtimeAmount:'', allowanceRate:'', allowanceAmount:'', overtimeDate:'', attMonth:'' });
    const empList = ref([]);
    const search = async () => { try { list.value = await $api.get('/api/allowance', { empId: fEmpId.value||undefined, overtimeType: fType.value||undefined, attMonth: fMonth.value||undefined }); } catch(e) { showErr(e.message); } };
    const openAdd = async () => { try { empList.value = await $api.get('/api/allowance/employees'); } catch(e){} editing.allowId = 'ALLOW-'+Date.now(); editing.empId=''; editing.overtimeType=''; editing.overtimeAmount=''; editing.allowanceRate=''; editing.allowanceAmount=''; editing.overtimeDate=''; editing.attMonth=''; showModal.value = true; };
    const doSave = async () => { try { await $api.post('/api/allowance', { ...editing }); showModal.value = false; showMsg('保存成功'); search(); } catch(e) { showErr(e.message); } };
    const doDelete = async (id) => { if (!confirm('确认删除？')) return; try { await $api.del('/api/allowance/'+id); showMsg('删除成功'); search(); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, fEmpId, fType, fMonth, search, showModal, editing, empList, openAdd, doSave, doDelete };
  }
};
// ===== MONTHLY SALARY LIST =====
const SalaryList = {
  template: `
<div>
  <div class="page-header"><h2>月工资管理</h2></div>
  <div class="card"><div class="card-body">
    <div class="search-bar">
      <input v-model="fEmpId" class="form-control" placeholder="员工ID" style="width:120px">
      <input v-model="fEmpName" class="form-control" placeholder="员工姓名" style="width:140px">
      <input type="month" v-model="fMonth" class="form-control" style="width:160px">
      <input type="number" v-model="fYear" class="form-control" placeholder="查询年份" style="width:120px" min="2020" max="2030">
      <button class="btn btn-primary" @click="search">查询</button>
      <button class="btn btn-outline-secondary" @click="fEmpId='';fEmpName='';fMonth='';fYear='';search()">重置</button>
    </div>
    <table class="table table-hover">
      <thead><tr><th>姓名</th><th>员工ID</th><th>部门</th><th>工种</th><th>月份</th><th>基本工资</th><th>津贴总额</th><th>扣款</th><th>实发工资</th><th>核算时间</th><th>操作员</th></tr></thead>
      <tbody>
        <tr v-for="s in list" :key="s.salaryId">
          <td>{{s.empName}}</td><td>{{s.empId}}</td><td>{{s.department}}</td><td>{{s.jobName}}</td><td>{{s.salaryMonth}}</td>
          <td>{{s.baseSalary}}</td><td>{{s.allowanceTotal}}</td><td>{{s.deduction}}</td><td><strong>{{s.netSalary}}</strong></td>
          <td>{{s.calcTime}}</td><td>{{s.operator}}</td>
        </tr>
      </tbody>
    </table>
    <div v-if="list.length===0" class="empty-state">暂无数据，请先生成月工资</div>
  </div></div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), fEmpId = ref(''), fEmpName = ref(''), fMonth = ref(''), fYear = ref('');
    const search = async () => { try { list.value = await $api.get('/api/salary/monthly', { empId: fEmpId.value||undefined, empName: fEmpName.value||undefined, salaryMonth: fMonth.value||undefined, year: fYear.value||undefined }); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, fEmpId, fEmpName, fMonth, fYear, search };
  }
};

// ===== SALARY GENERATE =====
const SalaryGenerate = {
  template: `
<div>
  <div class="page-header"><h2>生成月工资</h2></div>
  <div v-if="result" class="result-box">
    <div class="fw-bold mb-2">核算结果：{{result.month}}</div>
    <div class="result-item"><span>成功</span><strong class="text-success">{{result.success}}</strong></div>
    <div class="result-item"><span>失败</span><strong class="text-danger">{{result.fail}}</strong></div>
    <div class="result-item"><span>总计</span><strong>{{result.total}}</strong></div>
    <div v-if="result.errors&&result.errors.length" class="mt-2"><div v-for="e in result.errors" class="text-danger small">{{e}}</div></div>
  </div>
  <div class="card"><div class="card-body">
    <div style="max-width:400px">
      <div class="mb-3"><label class="form-label">工资月份</label><input type="month" v-model="month" class="form-control" required></div>
      <div class="mb-3"><label class="form-label">操作员</label><input v-model="operator" class="form-control" maxlength="10" required></div>
      <button class="btn btn-primary" @click="doGenerate" :disabled="loading">{{loading?'生成中...':'开始生成'}}</button>
      <p class="text-muted mt-2" style="font-size:12px">系统将遍历所有在职员工，根据考勤数据和津贴记录自动核算当月工资。</p>
    </div>
  </div></div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const month = ref(''), operator = ref('SYSTEM'), result = ref(null), loading = ref(false);
    const doGenerate = async () => {
      if (!month.value) return;
      loading.value = true;
      try { result.value = await $api.post('/api/salary/generate', { month: month.value, operator: operator.value }); showMsg('月工资生成完成'); } catch(e) { showErr(e.message); }
      loading.value = false;
    };
    return { month, operator, result, loading, doGenerate };
  }
};

// ===== BONUS LIST =====
const BonusList = {
  template: `
<div>
  <div class="page-header"><h2>年终奖金管理</h2></div>
  <div class="card">
    <div class="card-header"><span>奖金列表</span><router-link to="/bonus/generate" class="btn btn-primary btn-sm">生成年终奖金</router-link></div>
    <div class="card-body">
      <div class="search-bar">
        <input v-model="fEmpId" class="form-control" placeholder="员工ID" style="width:120px">
        <select v-model="fDept" class="form-select" style="width:140px"><option value="">全部部门</option><option>行政部</option><option>技术部</option><option>生产部</option><option>财务部</option><option>人事部</option></select>
        <input type="number" v-model="fYear" class="form-control" placeholder="年份" style="width:100px">
        <button class="btn btn-primary" @click="search">查询</button>
        <button class="btn btn-outline-secondary" @click="fEmpId='';fDept='';fYear='';search()">重置</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>姓名</th><th>员工ID</th><th>部门</th><th>年份</th><th>年度工资和</th><th>年度津贴和</th><th>年终奖金</th><th>核算时间</th></tr></thead>
        <tbody>
          <tr v-for="b in list" :key="b.bonusId">
            <td>{{b.empName}}</td><td>{{b.empId}}</td><td>{{b.department}}</td><td>{{b.year}}</td><td>{{b.totalSalary}}</td><td>{{b.totalAllowance}}</td><td><strong>{{b.bonusAmount}}</strong></td><td>{{b.calcTime}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const list = ref([]), fEmpId = ref(''), fDept = ref(''), fYear = ref('');
    const search = async () => { try { list.value = await $api.get('/api/salary/bonus', { empId: fEmpId.value||undefined, department: fDept.value||undefined, year: fYear.value||undefined }); } catch(e) { showErr(e.message); } };
    onMounted(search);
    return { list, fEmpId, fDept, fYear, search };
  }
};

// ===== BONUS GENERATE =====
const BonusGenerate = {
  template: `
<div>
  <div class="page-header"><h2>生成年终奖金</h2></div>
  <div v-if="result" class="result-box">
    <div class="fw-bold mb-2">核算结果：{{result.year}}年</div>
    <div class="result-item"><span>成功</span><strong class="text-success">{{result.success}}</strong></div>
    <div v-if="result.errors&&result.errors.length" class="mt-2"><div v-for="e in result.errors" class="text-danger small">{{e}}</div></div>
  </div>
  <div class="card"><div class="card-body">
    <div style="max-width:400px">
      <div class="mb-3"><label class="form-label">核算年份</label><input type="number" v-model="year" class="form-control" required min="2020" max="2030"></div>
      <div class="mb-3"><label class="form-label">操作员</label><input v-model="operator" class="form-control" maxlength="10" required></div>
      <button class="btn btn-primary" @click="doGenerate" :disabled="loading">{{loading?'生成中...':'开始生成'}}</button>
      <p class="text-muted mt-2" style="font-size:12px">年终奖金 = (本年度工资总和 + 本年度津贴总和) / 12</p>
    </div>
  </div></div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const year = ref(new Date().getFullYear()), operator = ref('SYSTEM'), result = ref(null), loading = ref(false);
    const doGenerate = async () => {
      if (!year.value) return;
      loading.value = true;
      try { result.value = await $api.post('/api/salary/bonus/generate', { year: year.value, operator: operator.value }); showMsg('年终奖金生成完成'); } catch(e) { showErr(e.message); }
      loading.value = false;
    };
    return { year, operator, result, loading, doGenerate };
  }
};
// ===== DEPARTMENT QUERY =====
const DeptQuery = {
  template: `
<div>
  <div class="page-header"><h2>部门工资查询</h2></div>
  <div class="card"><div class="card-body">
    <div class="search-bar">
      <select v-model="department" class="form-select" style="width:140px"><option value="">选择部门</option><option>行政部</option><option>技术部</option><option>生产部</option><option>财务部</option><option>人事部</option></select>
      <input type="month" v-model="salaryMonth" class="form-control" style="width:160px">
      <button class="btn btn-primary" @click="doQuery">查询</button>
    </div>
  </div></div>
  <div v-if="stats" style="margin-top:16px">
    <div class="dashboard-grid">
      <div class="card stat-card"><div class="label">部门工资总额</div><div class="value">{{stats.totalSalary}}</div></div>
      <div class="card stat-card"><div class="label">部门平均工资</div><div class="value">{{stats.avgSalary}}</div></div>
      <div class="card stat-card"><div class="label">部门人数</div><div class="value">{{stats.employeeCount}}</div></div>
    </div>
    <div class="card">
      <div class="card-header">{{stats.department}} - {{stats.month}} 工资明细</div>
      <div class="card-body">
        <table class="table table-hover">
          <thead><tr><th>员工ID</th><th>姓名</th><th>工种</th><th>基本工资</th><th>津贴总额</th><th>扣款</th><th>实发工资</th></tr></thead>
          <tbody>
            <tr v-for="s in stats.salaries" :key="s.salaryId">
              <td>{{s.empId}}</td><td>{{s.empName}}</td><td>{{s.jobName}}</td><td>{{s.baseSalary}}</td><td>{{s.allowanceTotal}}</td><td>{{s.deduction}}</td><td><strong>{{s.netSalary}}</strong></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const department = ref(''), salaryMonth = ref(''), stats = ref(null);
    const doQuery = async () => {
      if (!department.value || !salaryMonth.value) { showErr('请选择部门和月份'); return; }
      try { stats.value = await $api.get('/api/salary/dept', { department: department.value, salaryMonth: salaryMonth.value }); } catch(e) { showErr(e.message); }
    };
    return { department, salaryMonth, stats, doQuery };
  }
};

// ===== MONTHLY STATS =====
const MonthlyStats = {
  template: `
<div>
  <div class="page-header"><h2>按月工资统计</h2></div>
  <div class="card"><div class="card-body">
    <div class="search-bar">
      <input type="month" v-model="month" class="form-control" style="width:160px">
      <button class="btn btn-primary" @click="doQuery">统计</button>
    </div>
  </div></div>
  <div v-if="stats" style="margin-top:16px">
    <div class="dashboard-grid">
      <div class="card stat-card"><div class="label">当月工资总额</div><div class="value">{{stats.grandTotal}}</div></div>
      <div class="card stat-card"><div class="label">全员平均工资</div><div class="value">{{stats.overallAvg}}</div></div>
      <div class="card stat-card"><div class="label">发放人数</div><div class="value">{{stats.totalEmployees}}</div></div>
    </div>
    <div class="card">
      <div class="card-header">{{stats.month}} 各部门工资概况</div>
      <div class="card-body">
        <table class="table table-hover">
          <thead><tr><th>部门</th><th>工资总额</th><th>平均工资</th><th>人数</th></tr></thead>
          <tbody>
            <tr v-for="d in stats.deptStats" :key="d.department">
              <td>{{d.department}}</td><td>{{d.total}}</td><td>{{d.average}}</td><td>{{d.count}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>`,
  setup() { const showMsg = inject('showMsg'); const showErr = inject('showErr');
    const month = ref(''), stats = ref(null);
    const doQuery = async () => {
      if (!month.value) { showErr('请选择月份'); return; }
      try { stats.value = await $api.get('/api/salary/stats', { month: month.value }); } catch(e) { showErr(e.message); }
    };
    return { month, stats, doQuery };
  }
};

// ===== ROUTER =====
const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', component: Dashboard },
    { path: '/employees', component: EmployeeList },
    { path: '/jobs', component: JobList },
    { path: '/attendance', component: AttendanceList },
    { path: '/allowance', component: AllowanceList },
    { path: '/salaries', component: SalaryList },
    { path: '/salary/generate', component: SalaryGenerate },
    { path: '/bonuses', component: BonusList },
    { path: '/bonus/generate', component: BonusGenerate },
    { path: '/dept-query', component: DeptQuery },
    { path: '/monthly-stats', component: MonthlyStats },
  ]
});

// ===== APP =====
const app = createApp({
  setup() {
    const appMsg = ref(''), appErr = ref('');
    provide('showMsg', (m) => { appMsg.value = m; setTimeout(() => appMsg.value = '', 3000); });
    provide('showErr', (e) => { appErr.value = e; setTimeout(() => appErr.value = '', 5000); });
    return { appMsg, appErr };
  }
});

app.use(router);
app.mount('#app');