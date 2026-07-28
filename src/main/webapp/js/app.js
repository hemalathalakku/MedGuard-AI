/* ============================================================
   MedGuard AI - shared client-side behaviour (Module 15)
   1. Bootstrap form validation styling
   2. Loading spinner on form submit
   3. Toast helper
   4. Medicine-name autocomplete (talks to /user/autocomplete)
   ============================================================ */

// ---------- 1. Bootstrap validation ----------
document.querySelectorAll('.needs-validation').forEach(form => {
  form.addEventListener('submit', e => {
    if (!form.checkValidity()) {
      e.preventDefault();
      e.stopPropagation();
    }
    form.classList.add('was-validated');
  });
});

// ---------- 2. Spinner overlay ----------
(function () {
  const overlay = document.createElement('div');
  overlay.id = 'mgSpinner';
  overlay.innerHTML =
    '<div class="spinner-border text-primary" style="width:3rem;height:3rem" role="status"></div>';
  document.body.appendChild(overlay);

  document.querySelectorAll('form').forEach(f =>
    f.addEventListener('submit', () => {
      if (f.checkValidity()) overlay.classList.add('show');
    }));
})();

// ---------- 3. Toast helper ----------
function mgToast(message, type) {
  let box = document.getElementById('mgToasts');
  if (!box) {
    box = document.createElement('div');
    box.id = 'mgToasts';
    document.body.appendChild(box);
  }
  const el = document.createElement('div');
  el.className = 'toast align-items-center text-bg-' + (type || 'primary') + ' border-0 show mb-2';
  el.innerHTML = '<div class="d-flex"><div class="toast-body"></div>' +
    '<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button></div>';
  el.querySelector('.toast-body').textContent = message;   // textContent = XSS-safe
  box.appendChild(el);
  setTimeout(() => el.remove(), 4000);
}

// ---------- 4. Autocomplete for the medicine search box ----------
(function () {
  const input = document.getElementById('medSearch');
  const box = document.getElementById('acBox');
  if (!input || !box) return;                 // only active on the search page

  let timer = null;
  input.addEventListener('input', () => {
    clearTimeout(timer);
    timer = setTimeout(fetchNames, 250);      // debounce keystrokes
  });
  document.addEventListener('click', e => {
    if (!box.contains(e.target) && e.target !== input) box.innerHTML = '';
  });

  function fetchNames() {
    const term = input.value.trim();
    if (term.length < 2) { box.innerHTML = ''; return; }
    const ctx = document.querySelector('nav a.navbar-brand').getAttribute('href').replace(/\/user\/dashboard$/, '');
    fetch(ctx + '/user/autocomplete?term=' + encodeURIComponent(term))
      .then(r => r.json())
      .then(items => {
        box.innerHTML = '';
        items.forEach(it => {
          const a = document.createElement('a');
          a.className = 'list-group-item list-group-item-action';
          a.textContent = it.name;            // textContent = XSS-safe
          a.href = ctx + '/user/medicines?id=' + it.id;
          box.appendChild(a);
        });
      })
      .catch(() => { box.innerHTML = ''; });
  }
})();
