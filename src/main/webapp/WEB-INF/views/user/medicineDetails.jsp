<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>${m.medicineName} - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4" style="max-width:860px">
  <c:choose>
    <c:when test="${empty m}">
      <div class="alert alert-warning">Medicine not found.</div>
    </c:when>
    <c:otherwise>
      <div class="d-flex justify-content-between align-items-start mb-3">
        <div>
          <h4 class="mb-0">${m.medicineName}</h4>
          <div class="text-muted">${m.brand} &middot; ${m.genericName} &middot; ${m.manufacturer}</div>
        </div>
        <a href="${pageContext.request.contextPath}/user/safety?id=${m.medicineId}" class="btn btn-success">
          <i class="bi bi-shield-check"></i> Check Safety For Me
        </a>
      </div>

      <div class="card mg-glass p-4">
        <dl class="row mb-0">
          <dt class="col-sm-3">Description</dt><dd class="col-sm-9">${m.description}</dd>
          <dt class="col-sm-3">Uses</dt><dd class="col-sm-9">${m.uses}</dd>
          <dt class="col-sm-3">Dosage</dt><dd class="col-sm-9">${m.dosage}</dd>
          <dt class="col-sm-3">Ingredients</dt><dd class="col-sm-9">${m.ingredients}</dd>
          <dt class="col-sm-3">Side effects</dt><dd class="col-sm-9">${m.sideEffects}</dd>
          <dt class="col-sm-3">Contraindications</dt><dd class="col-sm-9">${m.contraindications}</dd>
          <dt class="col-sm-3">Warnings</dt><dd class="col-sm-9 text-danger">${m.warnings}</dd>
          <dt class="col-sm-3">Pregnancy category</dt><dd class="col-sm-9">${m.pregnancyCategory}</dd>
          <dt class="col-sm-3">Storage</dt><dd class="col-sm-9">${m.storageInstructions}</dd>
          <dt class="col-sm-3">Age restrictions</dt><dd class="col-sm-9">${m.ageRestrictions}</dd>
        </dl>
      </div>
      <a class="btn btn-link mt-3" href="${pageContext.request.contextPath}/user/medicines">&larr; Back to search</a>
    </c:otherwise>
  </c:choose>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
