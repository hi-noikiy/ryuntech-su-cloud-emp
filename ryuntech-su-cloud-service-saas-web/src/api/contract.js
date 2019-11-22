import request from '@/utils/request'

export function getList(query, data) {
  return request({
    url: '/saas/contract/list?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export function save(data) {
  return request({
    url: '/saas/contract/contractadd',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/saas/contract/contractupdate',
    method: 'post',
    data
  })
}

export function findById(contractId) {
  return request({
    url: '/saas/contract/contractFindById/' + contractId,
    method: 'get'
  })
}

export function del(contractId) {
  return request({
    url: '/saas/contract/' + contractId,
    method: 'delete'
  })
}

export function getContractListByCustomerId(customerId) {
  return request({
    url: '/saas/contract/byCustomerId/' + customerId,
    method: 'get',
  })
}

// // 回款计划批量插入
// export function backPlanInsertBatch(data) {
//   return request({
//     url: '/saas/collectionPlan/insertBatch',
//     method: 'post',
//     data
//   })
// }

export const upload = process.env.VUE_APP_BASE_API + '/saas/storage/local/upload'
