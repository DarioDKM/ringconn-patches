package com.gdjztech.ringconn.ui;

public class DashboardHtml {
    public static String getHtml() {
        return "<!DOCTYPE html>\n" +
"<html lang=\"de\">\n" +
"<head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">\n" +
"    <title>Intervals Direct Sync</title>\n" +
"    <style>\n" +
"        :root {\n" +
"            --bg: #0B0F19;\n" +
"            --card-bg: #161F30;\n" +
"            --card-border: #23334E;\n" +
"            --text-primary: #F8FAFC;\n" +
"            --text-secondary: #94A3B8;\n" +
"            --text-muted: #64748B;\n" +
"            --accent: #38BDF8;\n" +
"            --accent-hover: #0284C7;\n" +
"            --success: #10B981;\n" +
"            --warning: #F59E0B;\n" +
"            --danger: #EF4444;\n" +
"            --deep: #6366F1;\n" +
"            --rem: #06B6D4;\n" +
"            --light: #3B82F6;\n" +
"            --awake: #64748B;\n" +
"        }\n" +
"        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; -webkit-tap-highlight-color: transparent; }\n" +
"        body { background-color: var(--bg); color: var(--text-primary); padding: 16px; padding-bottom: 32px; }\n" +
"        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding-top: 8px; }\n" +
"        .brand { display: flex; align-items: center; gap: 10px; }\n" +
"        .brand-icon { width: 36px; height: 36px; border-radius: 10px; background: linear-gradient(135deg, #0284C7, #38BDF8); display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 18px; color: #fff; box-shadow: 0 4px 12px rgba(56,189,248,0.25); }\n" +
"        .brand h1 { font-size: 20px; font-weight: 700; letter-spacing: -0.5px; }\n" +
"        .brand p { font-size: 12px; color: var(--text-secondary); }\n" +
"        .close-btn { background: #1E293B; border: 1px solid var(--card-border); color: var(--text-secondary); width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; cursor: pointer; }\n" +
"        .nav-tabs { display: flex; background: #111827; border-radius: 12px; padding: 4px; margin-bottom: 20px; border: 1px solid var(--card-border); }\n" +
"        .nav-btn { flex: 1; padding: 10px 0; background: transparent; border: none; color: var(--text-secondary); font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer; transition: all 0.2s; }\n" +
"        .nav-btn.active { background: var(--accent); color: #0B0F19; }\n" +
"        .tab-content { display: none; }\n" +
"        .tab-content.active { display: block; }\n" +
"        .card { background: var(--card-bg); border: 1px solid var(--card-border); border-radius: 16px; padding: 16px; margin-bottom: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.2); }\n" +
"        .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }\n" +
"        .card-title { font-size: 14px; font-weight: 600; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.5px; }\n" +
"        .badge { padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; }\n" +
"        .badge-success { background: rgba(16,185,129,0.15); color: var(--success); }\n" +
"        .badge-warning { background: rgba(245,158,11,0.15); color: var(--warning); }\n" +
"        .metrics-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; margin-bottom: 16px; }\n" +
"        .metric-box { background: rgba(15,23,42,0.6); border: 1px solid var(--card-border); border-radius: 12px; padding: 12px; }\n" +
"        .metric-label { font-size: 11px; color: var(--text-muted); margin-bottom: 4px; }\n" +
"        .metric-val { font-size: 22px; font-weight: 700; color: var(--text-primary); letter-spacing: -0.5px; }\n" +
"        .metric-unit { font-size: 12px; font-weight: 400; color: var(--text-secondary); margin-left: 2px; }\n" +
"        .stage-bar { display: flex; height: 12px; border-radius: 6px; overflow: hidden; margin: 12px 0 8px 0; background: #0F172A; }\n" +
"        .stage-deep { background: var(--deep); }\n" +
"        .stage-rem { background: var(--rem); }\n" +
"        .stage-light { background: var(--light); }\n" +
"        .stage-awake { background: var(--awake); }\n" +
"        .stage-legend { display: flex; justify-content: space-between; font-size: 11px; color: var(--text-secondary); margin-top: 6px; }\n" +
"        .stage-dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; }\n" +
"        .btn-sync { width: 100%; padding: 14px; background: linear-gradient(135deg, #0284C7, #38BDF8); border: none; border-radius: 12px; color: #fff; font-size: 15px; font-weight: 700; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; box-shadow: 0 4px 16px rgba(56,189,248,0.3); transition: opacity 0.2s; }\n" +
"        .btn-sync:active { opacity: 0.85; }\n" +
"        .btn-secondary { width: 100%; padding: 12px; background: #1E293B; border: 1px solid var(--card-border); border-radius: 10px; color: var(--text-primary); font-size: 14px; font-weight: 600; cursor: pointer; margin-top: 10px; }\n" +
"        .form-group { margin-bottom: 14px; }\n" +
"        .form-label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }\n" +
"        .form-input { width: 100%; padding: 12px; background: #0F172A; border: 1px solid var(--card-border); border-radius: 10px; color: var(--text-primary); font-size: 14px; outline: none; }\n" +
"        .form-input:focus { border-color: var(--accent); }\n" +
"        .switch-row { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-top: 1px solid var(--card-border); }\n" +
"        .toast { position: fixed; bottom: 20px; left: 16px; right: 16px; padding: 14px 18px; border-radius: 12px; font-size: 13px; font-weight: 600; text-align: center; z-index: 1000; display: none; box-shadow: 0 8px 24px rgba(0,0,0,0.5); }\n" +
"        .toast-success { background: #065F46; color: #D1FAE5; border: 1px solid var(--success); }\n" +
"        .toast-error { background: #7F1D1D; color: #FEE2E2; border: 1px solid var(--danger); }\n" +
"        .history-item { background: rgba(15,23,42,0.5); border: 1px solid var(--card-border); border-radius: 12px; padding: 12px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }\n" +
"        .history-date { font-weight: 700; font-size: 14px; margin-bottom: 2px; }\n" +
"        .history-meta { font-size: 12px; color: var(--text-secondary); }\n" +
"        .history-score { font-size: 20px; font-weight: 800; color: var(--accent); text-align: right; }\n" +
"        .log-item { padding: 8px 12px; border-bottom: 1px solid var(--card-border); font-size: 12px; font-family: monospace; }\n" +
"        .log-time { color: var(--text-muted); margin-bottom: 2px; }\n" +
"        .log-msg { color: var(--text-primary); }\n" +
"    </style>\n" +
"</head>\n" +
"<body>\n" +
"    <div class=\"header\">\n" +
"        <div class=\"brand\">\n" +
"            <div class=\"brand-icon\">&#9889;</div>\n" +
"            <div>\n" +
"                <h1>Intervals Sync</h1>\n" +
"                <p id=\"status-line\">Bereit</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <button class=\"close-btn\" onclick=\"closeApp()\">&#10005;</button>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"nav-tabs\">\n" +
"        <button class=\"nav-btn active\" onclick=\"setTab(0)\">Dashboard</button>\n" +
"        <button class=\"nav-btn\" onclick=\"setTab(1)\">Historie</button>\n" +
"        <button class=\"nav-btn\" onclick=\"setTab(2)\">Setup</button>\n" +
"        <button class=\"nav-btn\" onclick=\"setTab(3)\">Logs</button>\n" +
"    </div>\n" +
"\n" +
"    <!-- DASHBOARD TAB -->\n" +
"    <div id=\"tab-0\" class=\"tab-content active\">\n" +
"        <div class=\"card\">\n" +
"            <div class=\"card-header\">\n" +
"                <span class=\"card-title\" id=\"dashboard-date\">Letzte Nacht</span>\n" +
"                <span class=\"badge badge-success\" id=\"sleep-score-badge\">Score: --</span>\n" +
"            </div>\n" +
"            <div class=\"metrics-grid\">\n" +
"                <div class=\"metric-box\">\n" +
"                    <div class=\"metric-label\">Schlafdauer</div>\n" +
"                    <div class=\"metric-val\" id=\"val-duration\">--<span class=\"metric-unit\"></span></div>\n" +
"                </div>\n" +
"                <div class=\"metric-box\">\n" +
"                    <div class=\"metric-label\">Ruhepuls (RHR)</div>\n" +
"                    <div class=\"metric-val\" id=\"val-rhr\">--<span class=\"metric-unit\">bpm</span></div>\n" +
"                </div>\n" +
"                <div class=\"metric-box\">\n" +
"                    <div class=\"metric-label\">HRV (SDNN)</div>\n" +
"                    <div class=\"metric-val\" id=\"val-hrv\">--<span class=\"metric-unit\">ms</span></div>\n" +
"                </div>\n" +
"                <div class=\"metric-box\">\n" +
"                    <div class=\"metric-label\">Hauttemperatur</div>\n" +
"                    <div class=\"metric-val\" id=\"val-temp\">--<span class=\"metric-unit\">&deg;C</span></div>\n" +
"                </div>\n" +
"            </div>\n" +
"            <div class=\"stage-bar\">\n" +
"                <div id=\"bar-deep\" class=\"stage-deep\" style=\"width: 25%\"></div>\n" +
"                <div id=\"bar-rem\" class=\"stage-rem\" style=\"width: 20%\"></div>\n" +
"                <div id=\"bar-light\" class=\"stage-light\" style=\"width: 45%\"></div>\n" +
"                <div id=\"bar-awake\" class=\"stage-awake\" style=\"width: 10%\"></div>\n" +
"            </div>\n" +
"            <div class=\"stage-legend\">\n" +
"                <span><span class=\"stage-dot\" style=\"background: var(--deep)\"></span>Tief <strong id=\"lbl-deep\">--</strong></span>\n" +
"                <span><span class=\"stage-dot\" style=\"background: var(--rem)\"></span>REM <strong id=\"lbl-rem\">--</strong></span>\n" +
"                <span><span class=\"stage-dot\" style=\"background: var(--light)\"></span>Leicht <strong id=\"lbl-light\">--</strong></span>\n" +
"                <span><span class=\"stage-dot\" style=\"background: var(--awake)\"></span>Wach <strong id=\"lbl-awake\">--</strong></span>\n" +
"            </div>\n" +
"        </div>\n" +
"\n" +
"        <button class=\"btn-sync\" id=\"sync-btn\" onclick=\"syncCurrentDay()\">\n" +
"            <span>&#8644;</span> Zu intervals.icu synchronisieren\n" +
"        </button>\n" +
"    </div>\n" +
"\n" +
"    <!-- HISTORIE TAB -->\n" +
"    <div id=\"tab-1\" class=\"tab-content\">\n" +
"        <div class=\"card\">\n" +
"            <div class=\"card-header\">\n" +
"                <span class=\"card-title\">Aufzeichnungen (30 Tage)</span>\n" +
"            </div>\n" +
"            <div id=\"history-list\">Lade Daten aus RingConn...</div>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <!-- SETUP TAB -->\n" +
"    <div id=\"tab-2\" class=\"tab-content\">\n" +
"        <div class=\"card\">\n" +
"            <div class=\"card-header\">\n" +
"                <span class=\"card-title\">Intervals.icu Zugangsdaten</span>\n" +
"            </div>\n" +
"            <div class=\"form-group\">\n" +
"                <label class=\"form-label\">Athlete ID (z.B. i557347)</label>\n" +
"                <input class=\"form-input\" id=\"cfg-athlete-id\" placeholder=\"i557347\">\n" +
"            </div>\n" +
"            <div class=\"form-group\">\n" +
"                <label class=\"form-label\">API-Key</label>\n" +
"                <input class=\"form-input\" id=\"cfg-api-key\" type=\"password\" placeholder=\"Intervals API Key\">\n" +
"            </div>\n" +
"            <div class=\"switch-row\">\n" +
"                <span class=\"form-label\" style=\"margin-bottom:0\">Automatischer Hintergrund-Sync</span>\n" +
"                <input type=\"checkbox\" id=\"cfg-auto-sync\" checked style=\"transform: scale(1.3);\">\n" +
"            </div>\n" +
"            <button class=\"btn-sync\" style=\"margin-top:14px;\" onclick=\"saveConfig()\">Einstellungen speichern</button>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <!-- LOGS TAB -->\n" +
"    <div id=\"tab-3\" class=\"tab-content\">\n" +
"        <div class=\"card\">\n" +
"            <div class=\"card-header\">\n" +
"                <span class=\"card-title\">Synchronisations-Protokoll</span>\n" +
"            </div>\n" +
"            <div id=\"logs-list\" style=\"max-height: 400px; overflow-y: auto;\">Keine Logs vorhanden.</div>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div id=\"toast\" class=\"toast\"></div>\n" +
"\n" +
"    <script>\n" +
"        let currentHistory = [];\n" +
"        let latestDate = '';\n" +
"\n" +
"        function showToast(msg, isError) {\n" +
"            const t = document.getElementById('toast');\n" +
"            t.innerText = msg;\n" +
"            t.className = 'toast ' + (isError ? 'toast-error' : 'toast-success');\n" +
"            t.style.display = 'block';\n" +
"            setTimeout(() => { t.style.display = 'none'; }, 3500);\n" +
"        }\n" +
"\n" +
"        function setTab(idx) {\n" +
"            document.querySelectorAll('.nav-btn').forEach((b, i) => b.classList.toggle('active', i === idx));\n" +
"            document.querySelectorAll('.tab-content').forEach((c, i) => c.classList.toggle('active', i === idx));\n" +
"            if (idx === 1) renderHistory();\n" +
"            if (idx === 3) loadLogs();\n" +
"        }\n" +
"\n" +
"        function closeApp() {\n" +
"            if (window.IntervalsBridge) window.IntervalsBridge.close();\n" +
"        }\n" +
"\n" +
"        function loadInitialData() {\n" +
"            if (!window.IntervalsBridge) return;\n" +
"            try {\n" +
"                const settings = JSON.parse(window.IntervalsBridge.getSettings());\n" +
"                document.getElementById('cfg-athlete-id').value = settings.athleteId || '';\n" +
"                document.getElementById('cfg-api-key').value = settings.apiKey || '';\n" +
"                document.getElementById('cfg-auto-sync').checked = settings.autoSync !== false;\n" +
"                if (settings.lastSyncStatus) {\n" +
"                    document.getElementById('status-line').innerText = settings.lastSyncStatus;\n" +
"                }\n" +
"                currentHistory = JSON.parse(window.IntervalsBridge.getSleepHistory(30));\n" +
"                if (currentHistory && currentHistory.length > 0) {\n" +
"                    displayDashboard(currentHistory[0]);\n" +
"                } else {\n" +
"                    document.getElementById('status-line').innerText = 'Keine Schlafdaten in ring_conn.db';\n" +
"                }\n" +
"            } catch (e) {\n" +
"                console.error(e);\n" +
"            }\n" +
"        }\n" +
"\n" +
"        function displayDashboard(d) {\n" +
"            latestDate = d.date || '';\n" +
"            document.getElementById('dashboard-date').innerText = d.date || 'Heute';\n" +
"            document.getElementById('sleep-score-badge').innerText = 'Score: ' + (d.score || '--');\n" +
"            const totalMins = d.sleepDurationMinutes || 0;\n" +
"            const h = Math.floor(totalMins / 60);\n" +
"            const m = totalMins % 60;\n" +
"            document.getElementById('val-duration').innerHTML = (h > 0 ? h + 'h ' : '') + m + '<span class=\"metric-unit\">m</span>';\n" +
"            document.getElementById('val-rhr').innerHTML = (d.restingHr || '--') + '<span class=\"metric-unit\">bpm</span>';\n" +
"            document.getElementById('val-hrv').innerHTML = (d.hrv || '--') + '<span class=\"metric-unit\">ms</span>';\n" +
"            const temp = d.tempOffset !== undefined ? (d.tempOffset > 0 ? '+' : '') + d.tempOffset : '--';\n" +
"            document.getElementById('val-temp').innerHTML = temp + '<span class=\"metric-unit\">&deg;C</span>';\n" +
"\n" +
"            const deep = d.deepMinutes || 0;\n" +
"            const rem = d.remMinutes || 0;\n" +
"            const light = d.lightMinutes || 0;\n" +
"            const awake = d.awakeMinutes || 0;\n" +
"            const sum = (deep + rem + light + awake) || 1;\n" +
"            document.getElementById('bar-deep').style.width = (deep / sum * 100) + '%';\n" +
"            document.getElementById('bar-rem').style.width = (rem / sum * 100) + '%';\n" +
"            document.getElementById('bar-light').style.width = (light / sum * 100) + '%';\n" +
"            document.getElementById('bar-awake').style.width = (awake / sum * 100) + '%';\n" +
"            document.getElementById('lbl-deep').innerText = deep + 'm';\n" +
"            document.getElementById('lbl-rem').innerText = rem + 'm';\n" +
"            document.getElementById('lbl-light').innerText = light + 'm';\n" +
"            document.getElementById('lbl-awake').innerText = awake + 'm';\n" +
"        }\n" +
"\n" +
"        function renderHistory() {\n" +
"            const container = document.getElementById('history-list');\n" +
"            if (!currentHistory || currentHistory.length === 0) {\n" +
"                container.innerHTML = '<p style=\"color:var(--text-muted);text-align:center;padding:16px;\">Keine Einträge gefunden</p>';\n" +
"                return;\n" +
"            }\n" +
"            let html = '';\n" +
"            currentHistory.forEach(item => {\n" +
"                const totalMins = item.sleepDurationMinutes || 0;\n" +
"                const h = Math.floor(totalMins / 60);\n" +
"                const m = totalMins % 60;\n" +
"                html += '<div class=\"history-item\">' +\n" +
"                    '<div>' +\n" +
"                        '<div class=\"history-date\">' + item.date + '</div>' +\n" +
"                        '<div class=\"history-meta\">' + h + 'h ' + m + 'm &bull; RHR: ' + (item.restingHr || '--') + ' bpm &bull; HRV: ' + (item.hrv || '--') + ' ms</div>' +\n" +
"                    '</div>' +\n" +
"                    '<div>' +\n" +
"                        '<div class=\"history-score\">' + (item.score || '--') + '</div>' +\n" +
"                        '<button onclick=\"syncSingle(\\'' + item.date + '\\')\" style=\"background:#0284C7;border:none;color:#fff;font-size:11px;padding:3px 8px;border-radius:6px;margin-top:4px;cursor:pointer;\">Sync</button>' +\n" +
"                    '</div>' +\n" +
"                '</div>';\n" +
"            });\n" +
"            container.innerHTML = html;\n" +
"        }\n" +
"\n" +
"        function loadLogs() {\n" +
"            if (!window.IntervalsBridge) return;\n" +
"            try {\n" +
"                const logs = JSON.parse(window.IntervalsBridge.getLogs());\n" +
"                const container = document.getElementById('logs-list');\n" +
"                if (!logs || logs.length === 0) {\n" +
"                    container.innerHTML = '<p style=\"color:var(--text-muted);padding:12px;\">Noch keine Log-Einträge vorhanden.</p>';\n" +
"                    return;\n" +
"                }\n" +
"                let html = '';\n" +
"                logs.forEach(l => {\n" +
"                    html += '<div class=\"log-item\"><div class=\"log-time\">' + l.time + '</div><div class=\"log-msg\">' + l.message + '</div></div>';\n" +
"                });\n" +
"                container.innerHTML = html;\n" +
"            } catch (e) { console.error(e); }\n" +
"        }\n" +
"\n" +
"        function saveConfig() {\n" +
"            if (!window.IntervalsBridge) return;\n" +
"            const athleteId = document.getElementById('cfg-athlete-id').value;\n" +
"            const apiKey = document.getElementById('cfg-api-key').value;\n" +
"            const autoSync = document.getElementById('cfg-auto-sync').checked;\n" +
"            window.IntervalsBridge.saveSettings(athleteId, apiKey, autoSync);\n" +
"            showToast('Einstellungen erfolgreich gespeichert!', false);\n" +
"        }\n" +
"\n" +
"        function syncCurrentDay() {\n" +
"            if (!latestDate) {\n" +
"                showToast('Kein Datum zum Synchronisieren gefunden.', true);\n" +
"                return;\n" +
"            }\n" +
"            syncSingle(latestDate);\n" +
"        }\n" +
"\n" +
"        function syncSingle(d) {\n" +
"            if (!window.IntervalsBridge) return;\n" +
"            const btn = document.getElementById('sync-btn');\n" +
"            btn.disabled = true;\n" +
"            btn.innerText = 'Synchronisiere ' + d + '...';\n" +
"            setTimeout(() => {\n" +
"                try {\n" +
"                    const res = JSON.parse(window.IntervalsBridge.syncDate(d));\n" +
"                    btn.disabled = false;\n" +
"                    btn.innerHTML = '<span>&#8644;</span> Zu intervals.icu synchronisieren';\n" +
"                    if (res.success) {\n" +
"                        showToast('Erfolgreich zu intervals.icu übertragen! (' + d + ')', false);\n" +
"                        document.getElementById('status-line').innerText = 'Letzter Sync: ' + d + ' OK';\n" +
"                    } else {\n" +
"                        showToast('Fehler: ' + res.message, true);\n" +
"                    }\n" +
"                } catch (e) {\n" +
"                    btn.disabled = false;\n" +
"                    btn.innerHTML = '<span>&#8644;</span> Zu intervals.icu synchronisieren';\n" +
"                    showToast('Fehler beim Aufruf: ' + e, true);\n" +
"                }\n" +
"            }, 100);\n" +
"        }\n" +
"\n" +
"        window.onload = loadInitialData;\n" +
"    </script>\n" +
"</body>\n" +
"</html>";
    }
}
