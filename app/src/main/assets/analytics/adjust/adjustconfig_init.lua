function before(hook, param)
    param:setArgument(3, true)
    param:setArgument(2, "production")
    param:setArgument(1, "123")
    return true
end