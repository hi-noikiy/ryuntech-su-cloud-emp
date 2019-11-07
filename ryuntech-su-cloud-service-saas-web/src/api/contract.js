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
    url: '/saas/contract',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/saas/contract/edit',
    method: 'put',
    data
  })
}

export function findById(contractId) {
  return request({
    url: '/saas/contract/' + contractId,
    method: 'get'
  })
}

export function del(contractId) {
  return request({
    url: '/saas/contract/' + contractId,
    method: 'delete'
  })
}

// 回款计划批量插入
export function backPlanInsertBatch(data) {
  return request({
    url: '/saas/collectionPlan/insertBatch',
    method: 'post',
    data
  })
}

export const upload = process.env.VUE_APP_BASE_API + '/saas/storage/local/upload'
