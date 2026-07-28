<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>MedGuard AI - Intelligent Medicine Safety</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-dark mg-navbar sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="index.jsp"><i class="bi bi-shield-plus"></i> MedGuard AI</a>
    <div class="ms-auto">
      <a href="login.jsp" class="btn btn-outline-light btn-sm me-2">Login</a>
      <a href="register.jsp" class="btn btn-success btn-sm">Register</a>
    </div>
  </div>
</nav>

<header class="mg-hero text-center text-white d-flex align-items-center">
  <div class="container py-5">
    <h1 class="display-4 fw-bold">Is this medicine <span class="text-success-emphasis">safe</span> for you?</h1>
    <p class="lead col-lg-8 mx-auto">
      MedGuard AI checks any medicine against your diseases, current medicines and
      allergies - and warns you before you take something dangerous.
    </p>
    <a href="register.jsp" class="btn btn-success btn-lg px-4 me-2">Get Started Free</a>
    <a href="admin/login.jsp" class="btn btn-outline-light btn-lg px-4">Admin</a>
  </div>
</header>

<section class="container py-5">
  <div class="row g-4 text-center">
    <div class="col-md-4">
      <div class="card mg-glass h-100 p-4">
        <i class="bi bi-capsule display-5 text-primary"></i>
        <h5 class="mt-3">Drug Interaction Engine</h5>
        <p class="text-muted small">Every new medicine is compared against everything you already take.</p>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card mg-glass h-100 p-4">
        <i class="bi bi-heart-pulse display-5 text-danger"></i>
        <h5 class="mt-3">Disease Warnings</h5>
        <p class="text-muted small">Warns when a medicine conflicts with heart, kidney, liver or other conditions.</p>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card mg-glass h-100 p-4">
        <i class="bi bi-exclamation-triangle display-5 text-warning"></i>
        <h5 class="mt-3">Allergy Checker</h5>
        <p class="text-muted small">Scans ingredient lists against your personal allergy list.</p>
      </div>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
