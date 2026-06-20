/* ============================================================
   ServiceSync — Admin API client
   All endpoints hit /api/admin/* (ADMIN role required)
   ============================================================ */

const API_BASE = (window.SERVICESYNC_API_BASE || 'http://localhost:8080');

const TOKEN_KEY = 'ss_token';
const NAME_KEY  = 'ss_name';
const EMAIL_KEY = 'ss_email';

const Auth = {
  saveSession({ token, name, email }) {
    localStorage.setItem(TOKEN_KEY, token);
    if (name)  localStorage.setItem(NAME_KEY,  name);
    if (email) localStorage.setItem(EMAIL_KEY, email);
  },
  getToken()  { return localStorage.getItem(TOKEN_KEY); },
  getName()   { return localStorage.getItem(NAME_KEY)  || (this.getEmail() ? this.getEmail().split('@')[0] : 'Admin'); },
  getEmail()  { return localStorage.getItem(EMAIL_KEY) || ''; },
  getRole() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g,'+').replace(/_/g,'/')));
      return payload.role || null;
    } catch { return null; }
  },
  isLoggedIn() {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g,'+').replace(/_/g,'/')));
      if (payload.exp && Date.now() >= payload.exp * 1000) { this.clear(); return false; }
      return true;
    } catch { return false; }
  },
  clear() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(NAME_KEY);
    localStorage.removeItem(EMAIL_KEY);
  },
  logout() { this.clear(); window.location.href = '../login.html'; },
  requireAdmin() {
    if (!this.isLoggedIn() || this.getRole() !== 'ADMIN') {
      window.location.href = '../login.html';
    }
  }
};

class ApiError extends Error {
  constructor(message, status, data) { super(message); this.status = status; this.data = data; }
}

const AdminApi = {
  async _request(path, { method = 'GET', body, parse = 'auto' } = {}) {
    const headers = { 'Content-Type': 'application/json' };
    if (Auth.getToken()) headers['Authorization'] = `Bearer ${Auth.getToken()}`;

    let res;
    try {
      res = await fetch(`${API_BASE}${path}`, {
        method, headers,
        body: body !== undefined ? JSON.stringify(body) : undefined
      });
    } catch {
      throw new ApiError(`Can't reach the server at ${API_BASE}. Make sure the Spring Boot backend is running.`, 0);
    }

    const rawText = await res.text();
    let data = rawText;
    if (parse !== 'text') {
      try { data = rawText ? JSON.parse(rawText) : null; } catch { data = rawText; }
    }
    if (!res.ok) {
      const message = (data && data.message) || (typeof data === 'string' ? data : `Request failed (${res.status})`);
      throw new ApiError(message, res.status, data);
    }
    return data;
  },

  get(path, opts)           { return this._request(path, { ...opts, method: 'GET' }); },
  post(path, body, opts)    { return this._request(path, { ...opts, method: 'POST', body }); },
  put(path, body, opts)     { return this._request(path, { ...opts, method: 'PUT',  body }); },
  del(path, opts)           { return this._request(path, { ...opts, method: 'DELETE' }); },

  // ---- Dashboard ----
  getDashboard()                          { return this.get('/api/admin/dashboard'); },

  // ---- Users ----
  getUsers(params = {}) {
    const q = new URLSearchParams(params).toString();
    return this.get(`/api/admin/users${q ? '?' + q : ''}`);
  },
  getUserById(id)                         { return this.get(`/api/admin/users/${id}`); },
  updateUserRole(id, role)                { return this.put(`/api/admin/users/${id}/role`, { role }); },
  deleteUser(id)                          { return this.del(`/api/admin/users/${id}`); },

  // ---- Complaints (admin) ----
  getComplaints(params = {}) {
    const q = new URLSearchParams(params).toString();
    return this.get(`/api/admin/complaints${q ? '?' + q : ''}`);
  },
  getComplaintById(id)                    { return this.get(`/api/admin/complaints/${id}`); },
  updateComplaintStatus(id, status)       { return this.put(`/api/admin/complaints/${id}/status`, { status }); },
  deleteComplaint(id)                     { return this.del(`/api/admin/complaints/${id}`); },
  getComplaintStats()                     { return this.get('/api/admin/complaints/statistics'); },

  // ---- Departments (admin) ----
  getAllDepartments()                      { return this.get('/api/admin/departments'); },
  createDepartment(payload)               { return this.post('/api/admin/departments', payload); },
  updateDepartment(id, payload)           { return this.put(`/api/admin/departments/${id}`, payload); },
  deleteDepartment(id)                    { return this.del(`/api/admin/departments/${id}`); },
  getDepartmentAnalytics()                { return this.get('/api/admin/departments/analytics'); },
};
