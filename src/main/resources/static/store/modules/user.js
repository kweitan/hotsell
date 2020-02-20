// 导入 types.js 文件
import types from "./../types";

const user = {
    namespaced: true,
    state ={
        adminToken:'' || localStorage.getItem("adminToken"),
        avatarUrl: '' || localStorage.getItem("avatarUrl"),
        sellerName: '' || localStorage.getItem("sellerName")
    },

// 定义 getters
    getters ={
        adminToken(state){
            return state.adminToken
        },
        avatarUrl(state){
            return state.avatarUrl
        },
        sellerName(state){
            return state.sellerName
        }
    },

    actions ={
        SETUSERINFO({ commit }, userinfo ){
            commit(types.SETUSERINFO,userinfo)
        },
        CLEARUSERINFO({commit}){
            commit(types.CLEARUSERINFO)
        }
    },

    mutations ={
        [types.SETUSERINFO](state,userinfo){
            state.adminToken = userinfo.adminToken;
            state.avatarUrl = userinfo.avatarUrl;
            state.sellerName = userinfo.sellerName;
            localStorage.setItem("adminToken", userinfo.adminToken);
            localStorage.setItem("avatarUrl", userinfo.avatarUrl);
            localStorage.setItem("sellerName", userinfo.sellerName);
        },
        [types.CLEARUSERINFO](state){
            state.adminToken = '';
            state.avatarUrl = '';
            state.sellerName = '';
            localStorage.setItem("adminToken","");
            localStorage.setItem("avatarUrl","");
            localStorage.setItem("sellerName", "");
        },
    }
}


// 最后统一导出
export default {
    user
}

// getter
// this.$store.getters['moduleB/bFullName'];
//
// ...mapGetters({
//     bGetter2: 'moduleB/bFullName'
// })
//
// // action
// this.$store.dispatch('moduleB/ASYNC_SET_NAME', { name: "JJ" });
//
// ...mapActions({
//     aSetAge: 'moduleB/ASYNC_SET_NAME',
// }),

// export default new vuex.Store({
//     getters,
//     actions,
//     mutations,
//     modules: {
//         user
//     }
// })

