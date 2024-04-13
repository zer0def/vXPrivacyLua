function after(hook, param)
    local res = param:getResult()
    local arg1 = param:getArgument(0)
    if res == nil or arg1 == nil then
        return false
    end

    if arg1 == 0 then
        local height = param:getSettingInt("display.height", 3100)
        if height == nil then
            return false
        end

        local xAxis = param:createXAxis(height)
        param:setResult(xAxis)
        return true, tostring(arg1), "Height: " .. tostring(height)
    elseif arg1 == 1 then
        local width = param:getSettingInt("display.width", 1400)
        if width == nil then
            return false
        end

        local yAxis = param:createYAxis(width)
        param:setResult(yAxis)
        return true, tostring(arg1), "Width: " .. tostring(width)
    end

    return false
end