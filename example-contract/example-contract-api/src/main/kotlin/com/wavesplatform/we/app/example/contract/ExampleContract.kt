package com.wavesplatform.we.app.example.contract

import com.wavesplatform.vst.contract.ContractAction
import com.wavesplatform.vst.contract.ContractInit
import com.wavesplatform.vst.contract.InvokeParam

interface ExampleContract {

    @ContractInit
    fun create()

    @ContractAction
    fun createCompany(
            @InvokeParam(name="name") name: String,
            @InvokeParam(name="data") data: String
    )
    @ContractAction
    fun createSupply(
            @InvokeParam(name="id") id: String
    )
    @ContractAction
    fun transport(
            @InvokeParam(name="id") id: String
    )
    @ContractAction
    fun finishSupply(
            @InvokeParam(name="id") id: String
    )

}
