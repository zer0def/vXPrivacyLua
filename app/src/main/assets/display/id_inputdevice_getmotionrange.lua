function after(hook, param)
    local res = param:getResult()
    local arg1 = param:getArgument(0)
    if res == nil or arg1 == nil then
        return false
    end

    log("[getMotionRange]=" .. tostring(arg1))

    if arg1 == 0 then
        local height = param:getSettingInt("display.height", 3100)
        log("InputDevice.height:" .. tostring(height))
        local xAxis = param:createXAxis(height)
        param:setResult(xAxis)
        return true
    elseif arg1 == 1 then
        local width = param:getSettingInt("display.width", 1400)
        log("InputDevice.width:" .. tostring(width))
        local yAxis = param:createYAxis(width)
        param:setResult(yAxis)
        return true
    end

    return false
end