package com.wavesplatform.we.app.example.contract.impl

import com.wavesplatform.vst.contract.data.ContractCall
import com.wavesplatform.vst.contract.mapping.Mapping
import com.wavesplatform.vst.contract.spring.annotation.ContractHandlerBean
import com.wavesplatform.vst.contract.state.ContractState
import com.wavesplatform.vst.contract.state.setValue
import com.wavesplatform.vst.contract.state.getValue
import com.wavesplatform.we.app.example.contract.ExampleContract

@ContractHandlerBean
class ExampleContractImpl(
    state: ContractState,
    private val call:ContractCall
) : ExampleContract {

    private var create: Boolean? by state

    private val companies: Mapping<Company> by state
    private val supplies: Mapping<Supply> by state

    override fun create() {
        create = true
    }

    override fun createCompany(name: String, data: String) {
        val owner = call.sender
        require(!companies.has(owner)) {
            "COMPANY_ALREADY_EXISTS"
        }
        val company = Company(
                name=name,
                data=data,
                owner = owner
        )
        companies.put(owner, company)
    }

    override fun createSupply(id:String) {
        val owner = call.sender
        require(companies.has(owner)) {
            "COMPANY_NOT_EXISTS"
        }
        val supply = Supply(
                id =id,
                comp1 =owner,
                start_time = call.timestamp
        )
        supplies.put(id, supply)
    }

    override fun transport(id: String) {
        val owner = call.sender
        require(companies.has(owner)) {
            "COMPANY_NOT_EXISTS"
        }
        val company = companies[owner]
        require(company.name=="Transport") {
            "WRONG_COMPANY"
        }
        val supply = supplies[id]
        val updateSupply = supply.copy(
                transport=owner,
                transport_time = call.timestamp
        )
        supplies.put(id, updateSupply)
    }

    override fun finishSupply(id:String) {
        val owner = call.sender
        require(companies.has(owner)) {
            "COMPANY_NOT_EXISTS"
        }
        val company = companies[owner]
        require(company.name=="comp2") {
            "WRONG_COMPANY"
        }
        val supply = supplies[id]
        val updateSupply = supply.copy(
                comp2=owner,
                finish_time = call.timestamp
        )
        supplies.put(id, updateSupply)
    }
}

data class Supply(
        val id: String,
        val comp1: String,
        val start_time: Long,
        val comp2: String? = null,
        val transport: String? = null,
        val transport_time: Long? = null,
        val finish_time: Long? = null
)

data class Company(
        val name: String,
        val data: String,
        val owner: String
)