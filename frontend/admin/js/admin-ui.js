/* ============================================================
   ServiceSync — Admin UI utilities (toasts, sidebar, helpers)
   ============================================================ */

function ensureToastRegion() {
  let r = document.getElementById('toast-region');
  if (!r) { r = document.createElement('div'); r.id = 'toast-region'; document.body.appendChild(r); }
  return r;
}

function toast(message, type = 'info', duration = 3600) {
  const region = ensureToastRegion();
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  const icon = type === 'success' ? '&#10003;' : type === 'error' ? '&#33;' : '&#8226;';
  el.innerHTML = `<span style="font-weight:700">${icon}</span><span>${escapeHtml(message)}</span>`;
  region.appendChild(el);
  setTimeout(() => { el.classList.add('leaving'); setTimeout(() => el.remove(), 260); }, duration);
}

function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function setButtonLoading(btn, isLoading) {
  if (!btn) return;
  btn.classList.toggle('loading', isLoading);
  btn.disabled = isLoading;
}

function timeAgo(dateStr) {
  if (!dateStr) return '—';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return '—';
  const diffMs = Date.now() - date.getTime();
  const mins = Math.floor(diffMs / 60000);
  if (mins < 1) return 'just now';
  if (mins < 60) return `${mins}m ago`;
  const hrs = Math.floor(mins / 60);
  if (hrs < 24) return `${hrs}h ago`;
  const days = Math.floor(hrs / 24);
  if (days < 30) return `${days}d ago`;
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
}

function formatDate(dateStr) {
  if (!dateStr) return '—';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return '—';
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' }) +
    ' · ' + date.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit' });
}

function ticketCode(id) { return 'SS-' + String(id).padStart(5, '0'); }

function statusLabel(status) {
  if (!status) return 'Open';
  return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
}

function statusBadgeClass(status) {
  const key = (status || 'open').toLowerCase();
  return `badge badge-${key}`;
}

function initials(name) {
  if (!name) return '?';
  return name.trim().split(/\s+/).slice(0, 2).map(w => w[0].toUpperCase()).join('');
}

/* ---------------- Confirm modal ---------------- */
function confirmAction({ title, message, confirmLabel = 'Confirm', danger = false }) {
  return new Promise((resolve) => {
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop';
    backdrop.innerHTML = `
      <div class="modal">
        <h3 style="margin-bottom:8px">${escapeHtml(title)}</h3>
        <p class="muted" style="margin-bottom:20px">${escapeHtml(message)}</p>
        <div style="display:flex;gap:10px;justify-content:flex-end">
          <button class="btn btn-ghost" data-act="cancel">Cancel</button>
          <button class="btn ${danger ? 'btn-danger' : 'btn-dark'}" data-act="ok">${escapeHtml(confirmLabel)}</button>
        </div>
      </div>`;
    document.body.appendChild(backdrop);
    requestAnimationFrame(() => backdrop.classList.add('show'));
    function close(result) { backdrop.classList.remove('show'); setTimeout(() => backdrop.remove(), 180); resolve(result); }
    backdrop.addEventListener('click', (e) => { if (e.target === backdrop) close(false); });
    backdrop.querySelector('[data-act="cancel"]').addEventListener('click', () => close(false));
    backdrop.querySelector('[data-act="ok"]').addEventListener('click', () => close(true));
  });
}

/* ---------------- Validation helpers ---------------- */
function showFieldError(inputEl, message) {
  inputEl.classList.add('error');
  const err = inputEl.parentElement.querySelector('.field-error');
  if (err) { err.textContent = message; err.classList.add('show'); }
}
function clearFieldError(inputEl) {
  inputEl.classList.remove('error');
  const err = inputEl.parentElement.querySelector('.field-error');
  if (err) err.classList.remove('show');
}
function isValidEmail(v) { return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v); }

/* ---------------- Count-up animation ---------------- */
function animateCount(el, target, duration = 800) {
  const startTime = performance.now();
  function tick(now) {
    const progress = Math.min((now - startTime) / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    el.textContent = Math.round(target * eased);
    if (progress < 1) requestAnimationFrame(tick); else el.textContent = target;
  }
  requestAnimationFrame(tick);
}

/* ---------------- Admin Sidebar + Shell ---------------- */
const ADMIN_NAV = [
  { href: 'dashboard.html',  label: 'Dashboard',    icon: 'grid'     },
  { href: 'users.html',      label: 'Users',         icon: 'users'    },
  { href: 'complaints.html', label: 'Complaints',    icon: 'inbox'    },
  { href: 'departments.html',label: 'Departments',   icon: 'building' },
  { href: 'profile.html',    label: 'Profile',       icon: 'user'     },
];

const ICONS = {
  grid:     '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7" rx="1.5"/><rect x="14" y="3" width="7" height="7" rx="1.5"/><rect x="3" y="14" width="7" height="7" rx="1.5"/><rect x="14" y="14" width="7" height="7" rx="1.5"/></svg>',
  users:    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>',
  inbox:    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-6"/><path d="M3 12h5l2 3h4l2-3h5"/><path d="M5.45 5.11 2 12"/><path d="M18.55 5.11 22 12"/><path d="M16 5H8L5.45 5.11"/></svg>',
  building: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="4" y="3" width="16" height="18" rx="1"/><path d="M9 8h1M14 8h1M9 12h1M14 12h1M9 16h1M14 16h1"/></svg>',
  user:     '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="8" r="4"/><path d="M4 21c0-4 4-6 8-6s8 2 8 6"/></svg>',
  logout:   '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>',
  menu:     '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="18" x2="21" y2="18"/></svg>',
  shield:   '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>',
};

function renderAdminShell(activePage) {
  const name  = Auth.getName();
  const role  = Auth.getRole() || 'ADMIN';

  const links = ADMIN_NAV.map(item => `
    <a class="nav-link ${item.href === activePage ? 'active' : ''}" href="${item.href}">
      ${ICONS[item.icon]}<span>${item.label}</span>
    </a>`).join('');

  const shellHtml = `
    <div class="scrim" id="scrim"></div>
    <aside class="sidebar" id="sidebar">
      <div class="brand">
        <div class="brand-mark">${ICONS.shield}</div>
        <div class="brand-name">Service<span>Sync</span></div>
      </div>
      <div class="admin-badge-strip">
        <span class="admin-badge">&#9673; Admin Console</span>
      </div>
      <nav class="nav-group">${links}</nav>
      <div class="sidebar-foot">
        <div class="user-chip">
          <div class="avatar">${initials(name)}</div>
          <div class="who">
            <div class="name">${escapeHtml(name)}</div>
            <div class="role">${role.toLowerCase()}</div>
          </div>
        </div>
        <a class="nav-link" id="logout-btn">${ICONS.logout}<span>Sign out</span></a>
      </div>
    </aside>`;

  document.getElementById('shell-mount').insertAdjacentHTML('beforebegin', shellHtml);

  document.getElementById('logout-btn').addEventListener('click', async () => {
    const ok = await confirmAction({ title: 'Sign out', message: 'You will need to log in again to access the Admin Console.', confirmLabel: 'Sign out' });
    if (ok) Auth.logout();
  });

  const menuToggle = document.getElementById('menu-toggle');
  const sidebar    = document.getElementById('sidebar');
  const scrim      = document.getElementById('scrim');
  if (menuToggle) menuToggle.addEventListener('click', () => { sidebar.classList.add('open'); scrim.classList.add('show'); });
  if (scrim)      scrim.addEventListener('click', () => { sidebar.classList.remove('open'); scrim.classList.remove('show'); });
}
