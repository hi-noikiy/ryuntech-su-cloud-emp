import request from '@/utils/request'

export function getEmployeeList(query, data) {
  return request({
    url: '/saas/employee/list?pageCode=' + query.page + '&pageSize=' + query.limit,
    method: 'post',
    data
  })
}

export function updateStatus(data) {
  return request({
    url: '/saas/employee/updateStatus',
    method: 'post',
    data
  })
}

export function del(data) {
  return request({
    url: '/saas/employee/del',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/saas/employee/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/saas/employee/edit',
    method: 'post',
    data
  })
}
