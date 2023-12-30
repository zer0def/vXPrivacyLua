function before(hook, param)
    log("HEIGHT")
    local height = param:getSettingInt("display.height", 3100)
    log("HEIGHT: " .. height)
    param:setResult(height)
    return true
end