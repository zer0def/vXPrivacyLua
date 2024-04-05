function before(hook, param)
    log("Chromium BatteryMonitorImp invoke...")
	param:setResult(null)
	return true
end