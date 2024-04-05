function after(hook, param)
    local arg1 = param:getArgument(0)
    if arg1 == nil then
        return false
    end

    log("Display.getRectSize")

    arg1.left = 0;
    arg1.top = 0;
    -- Bottom-right corner is defined by width and height
    arg1.right = arg1.left + param:getSettingInt("display.width", 1300)
    arg1.bottom = arg1.top + param:getSettingInt("display.height", 3100)
    return true
end