import request from '@/utils/request'

export function getList(query, data) {
  return request({
    url: '/saas/collection/list?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export function del(collectionId) {
  return request({
    url: '/saas/collection/' + collectionId,
    method: 'delete'
  })
}
