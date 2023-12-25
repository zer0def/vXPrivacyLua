function before(hook, param)
	log("KEYYYYBOARD OPTOONSS SETTTTTTTT")

    local e_info = luajava.bindClass("android.view.inputmethod.EditorInfo")
    local fake = e_info.IME_FLAG_NO_PERSONALIZED_LEARNING
    --Constant Value: 16777216 (0x01000000)
    --param:setResult(fake)
    param:setArgument(0, fake)
    return true
end