<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Dashboard - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">

  <h4 class="mb-3">Welcome back, <span class="text-primary">${sessionScope.userName}</span> 👋</h4>

  <div class="row g-3 mb-4">
    <div class="col-6 col-lg-3"><div class="card mg-widget p-3"><div class="small text-muted">Current medicines</div><div class="fs-3 fw-bold">${currentMeds.size()}</div></div></div>
    <div class="col-6 col-lg-3"><div class="card mg-widget p-3"><div class="small text-muted">Active reminders</div><div class="fs-3 fw-bold">${reminders.size()}</div></div></div>
    <div class="col-6 col-lg-3"><div class="card mg-widget p-3"><div class="small text-muted">Diseases on profile</div><div class="fs-3 fw-bold">${diseases.size()}</div></div></div>
    <div class="col-6 col-lg-3"><div class="card mg-widget p-3"><div class="small text-muted">Allergies</div><div class="fs-3 fw-bold">${allergies.size()}</div></div></div>
  </div>

  <div class="row g-4">
    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6><i class="bi bi-alarm text-primary"></i> Today's Reminders</h6>
        <c:if test="${empty reminders}"><div class="text-muted small">No reminders yet - add one on the Reminders page.</div></c:if>
        <ul class="list-group list-group-flush">
          <c:forEach var="r" items="${reminders}">
            <c:if test="${r.active}">
              <li class="list-group-item bg-transparent d-flex justify-content-between">
                <span>${r.medicineName} <span class="badge text-bg-light">${r.frequency}</span></span>
                <span class="fw-bold">${r.remindTime}</span>
              </li>
            </c:if>
          </c:forEach>
        </ul>
      </div>
    </div>

    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6><i class="bi bi-capsule text-success"></i> Current Medicines</h6>
        <c:if test="${empty currentMeds}"><div class="text-muted small">Nothing added yet.</div></c:if>
        <ul class="list-group list-group-flush">
          <c:forEach var="cm" items="${currentMeds}">
            <li class="list-group-item bg-transparent">${cm.medicineName}
              <span class="text-muted small">${cm.dose} ${cm.frequency}</span></li>
          </c:forEach>
        </ul>
      </div>
    </div>

    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6><i class="bi bi-heart-pulse text-danger"></i> Health Profile</h6>
        <div class="small text-muted mb-1">Diseases</div>
        <div class="mb-2">
          <c:if test="${empty diseases}"><span class="text-muted small">None recorded.</span></c:if>
          <c:forEach var="d" items="${diseases}"><span class="badge text-bg-danger-subtle text-danger me-1">${d.diseaseName}</span></c:forEach>
        </div>
        <div class="small text-muted mb-1">Allergies</div>
        <div>
          <c:if test="${empty allergies}"><span class="text-muted small">None recorded.</span></c:if>
          <c:forEach var="a" items="${allergies}"><span class="badge text-bg-warning-subtle text-warning-emphasis me-1">${a.value}</span></c:forEach>
        </div>
      </div>
    </div>

    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6><i class="bi bi-clock-history text-secondary"></i> Recent Safety Checks</h6>
        <c:if test="${empty recent}"><div class="text-muted small">No checks yet - search a medicine and press "Check Safety".</div></c:if>
        <ul class="list-group list-group-flush">
          <c:forEach var="h" items="${recent}">
            <li class="list-group-item bg-transparent d-flex justify-content-between">
              <span>${h.medicineName}</span>
              <span class="badge ${h.result == 'SAFE' ? 'text-bg-success' : h.result == 'CAUTION' ? 'text-bg-warning' : 'text-bg-danger'}">${h.result}</span>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
