<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Reports - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3"><i class="bi bi-bar-chart text-primary"></i> Reports &amp; Analytics</h4>

  <div class="row g-3 mb-4">
    <div class="col-md-4"><div class="card mg-widget p-3 text-center"><div class="fs-3 fw-bold">${totalUsers}</div><div class="text-muted small">Users</div></div></div>
    <div class="col-md-4"><div class="card mg-widget p-3 text-center"><div class="fs-3 fw-bold">${totalMedicines}</div><div class="text-muted small">Medicines</div></div></div>
    <div class="col-md-4"><div class="card mg-widget p-3 text-center"><div class="fs-3 fw-bold">${totalChecks}</div><div class="text-muted small">Safety checks</div></div></div>
  </div>

  <div class="row g-4">
    <div class="col-lg-5">
      <div class="card mg-glass p-3"><h6>Verdict distribution</h6><canvas id="resultChart"></canvas></div>
    </div>
    <div class="col-lg-7">
      <div class="card mg-glass p-3"><h6>Most checked medicines</h6><canvas id="topChart"></canvas></div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
<script>
/* Data is rendered server-side by JSTL into plain JS arrays. */
const resultLabels = [<c:forEach var="s" items="${resultStats}" varStatus="i">"${s[0]}"<c:if test="${!i.last}">,</c:if></c:forEach>];
const resultCounts = [<c:forEach var="s" items="${resultStats}" varStatus="i">${s[1]}<c:if test="${!i.last}">,</c:if></c:forEach>];
const topLabels    = [<c:forEach var="t" items="${topMedicines}" varStatus="i">"${t[0]}"<c:if test="${!i.last}">,</c:if></c:forEach>];
const topCounts    = [<c:forEach var="t" items="${topMedicines}" varStatus="i">${t[1]}<c:if test="${!i.last}">,</c:if></c:forEach>];

const colorFor = l => l === 'SAFE' ? '#198754' : l === 'CAUTION' ? '#ffc107' : '#dc3545';

new Chart(document.getElementById('resultChart'), {
  type: 'doughnut',
  data: { labels: resultLabels,
          datasets: [{ data: resultCounts, backgroundColor: resultLabels.map(colorFor) }] }
});
new Chart(document.getElementById('topChart'), {
  type: 'bar',
  data: { labels: topLabels,
          datasets: [{ label: 'Checks', data: topCounts, backgroundColor: '#0d6efd' }] },
  options: { plugins: { legend: { display: false } } }
});
</script>
</body>
</html>
