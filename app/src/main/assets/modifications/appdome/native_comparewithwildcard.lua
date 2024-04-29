function before(hook, param)
    local argOne = param:getArgument(0)
    local argTwo = param:getArgument(1)
    if argOne == nil or argTwo == nil then
        return false
    end
    log("AppDome [checkEqualStrings] a=" .. argOne .. " b=" .. argTwo)
    return false
end