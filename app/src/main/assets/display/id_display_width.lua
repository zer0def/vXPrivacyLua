function before(hook, param)
    log("WIDTH")
    local width = param:getSettingInt("display.width", 1400)
    log("WIDTH: " .. width)
    param:setResult(width)
    return true
end