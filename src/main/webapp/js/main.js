/* MedGuard AI - shared front-end helpers (Module 1)
   Later modules add their own page scripts; this file holds
   things every page can use. */

// Smooth-scroll for same-page anchor links (e.g. "Features" in the navbar)
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll('a[href^="#"]').forEach(link => {
    link.addEventListener("click", e => {
      const target = document.querySelector(link.getAttribute("href"));
      if (target) { e.preventDefault(); target.scrollIntoView({ behavior: "smooth" }); }
    });
  });
});
