# ServiceSync — Frontend

A full interactive frontend for the Grievance Management System backend, built with
plain HTML/CSS/JS (no build step, no npm install — just open it).

## What's included

- `index.html` — landing page with live stats pulled from `/api/dashboard/stats`
- `register.html` / `login.html` — auth pages (JWT stored in `localStorage`)
- `dashboard.html` — role-aware stats + recent complaints
- `complaints.html` — searchable/filterable complaint list
- `new-complaint.html` — complaint filing form (USER role)
- `complaint-detail.html` — full record, status timeline, status-update/delete actions for staff
- `departments.html` — department directory + "add department" (AGENT/SUPERVISOR/ADMIN)
- `profile.html` — account info + sign out
- `css/styles.css` — all styling, design tokens, and animations
- `js/config.js` — set your backend URL here
- `js/api.js` — fetch wrapper for every backend endpoint
- `js/ui.js` — toasts, modals, sidebar nav, small helpers

Role-based nav/actions are derived from the `role` claim inside the JWT itself (the
backend doesn't expose a `/me` endpoint, so this is decoded client-side).

## 1. Run the backend

```
cd grievance-management-system
mvn spring-boot:run
```

It starts on `http://localhost:8080` (per `application.properties`). Make sure
PostgreSQL is running and `grievancesystem_db` exists.

**One backend file was added for you:** `WebConfig.java` (in
`src/main/java/com/ishika/grievance/config/`). Your backend had no CORS
configuration, so a browser-based frontend on a different port would be blocked.
This file enables CORS for all origins — copy it into your project if it isn't
there already, then rebuild.

## 2. Run the frontend

No build, no install. Just serve the folder as static files (opening the file
directly with `file://` will break `fetch` calls in some browsers, so use a tiny
static server):

```
cd frontend
python3 -m http.server 5500
```

Then open `http://localhost:5500`.

(VS Code's "Live Server" extension, or `npx serve`, work the same way.)

If your backend runs anywhere other than `http://localhost:8080`, edit
`js/config.js`.

## Notes on backend behavior this UI works around

- `/api/auth/login` returns either a raw JWT string or a plain-text error
  (`"User Not Found"`, `"Invalid Password"`) — the frontend detects which.
- The JWT only carries `email` and `role`, not a user ID or name, so the
  "name" shown in the sidebar/profile is just what you typed at registration,
  stored locally — it isn't re-fetched from the server.
- `GET /api/complaints` returns *all* complaints (there's no per-user filter
  endpoint), so `complaints.html` shows the full list for every role.
- `POST /api/complaints` (used by the filing form) doesn't attach a user or
  department object server-side — only `POST /api/complaints/user/{userId}`
  does that, and it requires a numeric user ID the JWT doesn't expose. If you
  want complaints properly linked to the logged-in user, you'll want to add a
  `GET /api/users/me`-style endpoint and switch `new-complaint.html` to call
  `createComplaintForUser`.
- Departments are fetched by guessing IDs 1–10 (`GET /api/departments/{id}`)
  since there's no "list all departments" endpoint.
