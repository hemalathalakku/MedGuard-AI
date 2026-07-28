<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>My Profile - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4" style="max-width:760px">
  <h4 class="mb-3"><i class="bi bi-person-circle text-primary"></i> My Profile</h4>

  <c:if test="${param.saved == '1'}"><div class="alert alert-success py-2">Profile saved.</div></c:if>
  <c:if test="${param.error == 'name'}"><div class="alert alert-danger py-2">Name cannot be empty.</div></c:if>

  <div class="card mg-glass p-4">
    <div class="d-flex align-items-center mb-3">
      <c:choose>
        <c:when test="${not empty u.photo}">
          <img src="${pageContext.request.contextPath}/uploads/${u.photo}" class="rounded-circle me-3" width="72" height="72" style="object-fit:cover" alt="photo">
        </c:when>
        <c:otherwise><i class="bi bi-person-circle display-4 text-secondary me-3"></i></c:otherwise>
      </c:choose>
      <div><div class="fw-bold">${u.fullName}</div><div class="text-muted small">${u.email}</div></div>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/user/profile"
          enctype="multipart/form-data" class="row g-3 needs-validation" novalidate>
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <div class="col-md-6"><label class="form-label">Full name</label>
        <input name="fullName" value="${u.fullName}" class="form-control" required></div>
      <div class="col-md-3"><label class="form-label">Age</label>
        <input name="age" type="number" min="0" max="120" value="${u.age}" class="form-control"></div>
      <div class="col-md-3"><label class="form-label">Gender</label>
        <select name="gender" class="form-select">
          <option value="" ${empty u.gender ? 'selected' : ''}>-</option>
          <option ${u.gender == 'Male' ? 'selected' : ''}>Male</option>
          <option ${u.gender == 'Female' ? 'selected' : ''}>Female</option>
          <option ${u.gender == 'Other' ? 'selected' : ''}>Other</option>
        </select></div>
      <div class="col-md-3"><label class="form-label">Height (cm)</label>
        <input name="heightCm" type="number" step="0.1" value="${u.heightCm}" class="form-control"></div>
      <div class="col-md-3"><label class="form-label">Weight (kg)</label>
        <input name="weightKg" type="number" step="0.1" value="${u.weightKg}" class="form-control"></div>
      <div class="col-md-3"><label class="form-label">Blood group</label>
        <input name="bloodGroup" value="${u.bloodGroup}" class="form-control" placeholder="e.g. B+"></div>
      <div class="col-md-3"><label class="form-label">Emergency contact</label>
        <input name="emergencyContact" value="${u.emergencyContact}" class="form-control"></div>
      <div class="col-12"><label class="form-label">Address</label>
        <input name="address" value="${u.address}" class="form-control"></div>
      <div class="col-12"><label class="form-label">Profile photo <span class="text-muted small">(max 2 MB)</span></label>
        <input name="photo" type="file" accept="image/*" class="form-control"></div>
      <div class="col-12"><button class="btn btn-primary">Save profile</button></div>
    </form>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
