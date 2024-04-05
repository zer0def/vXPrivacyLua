--computeChargeTimeRemaining

function after(hook, param)
	log("computeChargeTimeRemaining=0")
	param:setResult(0)
	return true
end