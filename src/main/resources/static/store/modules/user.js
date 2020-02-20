// 导入 types.js 文件
import types from "./../types";

const state ={
    adminToken:'',
    avatarUrl: '',
    sellerName: ''
}

// 定义 getters
var getters ={
    adminToken(state){
        return state.adminToken
    },
    avatarUrl(state){
        return state.avatarUrl
    },
    sellerName(state){
        return state.sellerName
    }
}

const actions ={
    SETADMINTOKEN({ commit, adminToken }){
        commit(types.SETADMINTOKEN,adminToken)
    },
    SETSELLERNAME({commit,sellerName}){
        commit(types.SETSELLERNAME,sellerName)
    },
    SETAVATARURL({commit,avatarUrl}){
        commit(types.SETAVATARURL,avatarUrl)
    }
}

const mutations ={
    [types.SETADMINTOKEN](state,adminToken){
        state.adminToken = adminToken
    },
    [types.SETSELLERNAME](state,sellerName){
        state.sellerName = sellerName
    },
    [types.SETAVATARURL](state,avatarUrl){
        state.avatarUrl = avatarUrl
    }
}
// 最后统一导出
export default {
    state,
    getters,
    actions,
    mutations
}