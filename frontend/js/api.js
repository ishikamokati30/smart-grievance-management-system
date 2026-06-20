/* ============================================================
   ServiceSync — API client
   ============================================================ */
const API_BASE = (window.SERVICESYNC_API_BASE || 'http://localhost:8080');

const TOKEN_KEY = 'ss_token';
const NAME_KEY = 'ss_name';
const EMAIL_KEY = 'ss_email';

const Auth = {
  saveSession({ token, name, email }) {
    localStorage.setItem(TOKEN_KEY, token);
    if (name) localStorage.setItem(NAME_KEY, name);
    if (email) localStorage.setItem(EMAIL_KEY, email);
  },
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },
  getName() {
    return localStorage.getItem(NAME_KEY) || (this.getEmail() ? this.getEmail().split('@')[0] : 'User');
  },
  getEmail() {
    return localStorage.getItem(EMAIL_KEY) || '';
  },
  getRole() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return payload.role || null;
    } catch (e) {
      return null;
    }
  },
  isLoggedIn() {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      if (payload.exp && Date.now() >= payload.exp * 1000) {
        this.clear();
        return false;
      }
      return true;
    } catch (e) {
      return false;
    }
  },
  clear() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(NAME_KEY);
    localStorage.removeItem(EMAIL_KEY);
  },
  logout() {
    this.clear();
    window.location.href = 'login.html';
  },
  requireAuth() {
    if (!this.isLoggedIn()) {
      window.location.href = 'login.html';
    }
  },
  redirectIfLoggedIn() {
    if (this.isLoggedIn()) {
      const role = this.getRole();
      window.location.href = role === 'ADMIN' ? 'admin/dashboard.html' : 'dashboard.html';
    }
  }
};

/**
 * Looks like a raw JWT (three base64url segments).
 */
function looksLikeJwt(str) {
  return typeof str === 'string' && /^[\w-]+\.[\w-]+\.[\w-]+$/.test(str.trim());
}

const Api = {
  async _request(path, { method = 'GET', body, auth = true, parse = 'auto' } = {}) {
    const headers = { 'Content-Type': 'application/json' };
    if (auth && Auth.getToken()) {
      headers['Authorization'] = `Bearer ${Auth.getToken()}`;
    }

    let res;
    try {
      res = await fetch(`${API_BASE}${path}`, {
        method,
        headers,
        body: body !== undefined ? JSON.stringify(body) : undefined
      });
    } catch (networkErr) {
      throw new ApiError(
        `Can't reach the server at ${API_BASE}. Make sure the Spring Boot backend is running.`,
        0
      );
    }

    const rawText = await res.text();
    let data = rawText;
    if (parse !== 'text') {
      try {
        data = rawText ? JSON.parse(rawText) : null;
      } catch (e) {
        data = rawText; // backend returned a plain string, not JSON
      }
    }

    if (!res.ok) {
      const message = (data && data.message) || (typeof data === 'string' ? data : `Request failed (${res.status})`);
      throw new ApiError(message, res.status, data);
    }

    return data;
  },

  get(path, opts) { return this._request(path, { ...opts, method: 'GET' }); },
  post(path, body, opts) { return this._request(path, { ...opts, method: 'POST', body }); },
  put(path, body, opts) { return this._request(path, { ...opts, method: 'PUT', body }); },
  del(path, opts) { return this._request(path, { ...opts, method: 'DELETE' }); },

  // ---- Auth ----
  async register({ name, email, password }) {
    return this._request('/api/auth/register', { method: 'POST', body: { name, email, password }, auth: false, parse: 'text' });
  },
  async login({ email, password }) {
    const result = await this._request('/api/auth/login', { method: 'POST', body: { email, password }, auth: false, parse: 'text' });
    if (!looksLikeJwt(result)) {
      // backend returns plain strings like "User Not Found" / "Invalid Password" on failure
      throw new ApiError(result || 'Login failed', 401);
    }
    return result;
  },

  // ---- Complaints ----
  getAllComplaints() { return this.get('/api/complaints'); },
  getComplaint(id) { return this.get(`/api/complaints/${id}`); },
  createComplaint(payload) { return this._request('/api/complaints', { method: 'POST', body: payload, parse: 'text' }); },
  createComplaintForUser(userId, payload) { return this._request(`/api/complaints/user/${userId}`, { method: 'POST', body: payload, parse: 'text' }); },
  updateComplaintStatus(id, status) { return this._request(`/api/complaints/${id}`, { method: 'PUT', body: { status }, parse: 'text' }); },
  deleteComplaint(id) { return this._request(`/api/complaints/${id}`, { method: 'DELETE', parse: 'text' }); },

  // ---- Departments ----
  getDepartment(id) { return this.get(`/api/departments/${id}`); },
  createDepartment(payload) { return this._request('/api/departments', { method: 'POST', body: payload, parse: 'text' }); },

  // ---- Dashboard ----
  getDashboardStats() { return this.get('/api/dashboard/stats'); }
};

class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.status = status;
    this.data = data;
  }
}
