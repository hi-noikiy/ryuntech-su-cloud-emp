import request from '@/utils/request'

export function getList(query, data) {
  return request({
    url: '/saas/customer/list?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export function save(data) {
  return request({
    url: '/saas/customer',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/saas/customer/edit',
    method: 'put',
    data
  })
}

export function findById(customerId) {
  return request({
    url: '/saas/customer/' + customerId,
    method: 'get'
  })
}

export function del(customerId) {
  return request({
    url: '/saas/customer/' + customerId,
    method: 'delete'
  })
}

export function getCustomerMap(name) {
  return request({
    url: '/saas/customer/getCustomerMap',
    method: 'post',
    data: { customerName: name }
  })
}

export function getEmployeeList(query, data) {
  return request({
    url: '/saas/employee/limitList?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export function riskByCustomerId(customerId) {
  return request({
    url: '/saas/employee/limitList?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export const upload = process.env.VUE_APP_BASE_API + '/saas/storage/local/upload'
