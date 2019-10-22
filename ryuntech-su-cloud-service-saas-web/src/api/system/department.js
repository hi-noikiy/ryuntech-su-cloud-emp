import request from '@/utils/request'

export function getDepartmentList() {
  return request({
    url: '/saas/department/list',
    method: 'get'
  })
}

export function addDepartment(data) {
  return request({
    url: '/saas/department/add',
    method: 'post',
    data
  })
}

export function editDepartment(data) {
  return request({
    url: '/saas/department/edit',
    method: 'post',
    data
  })
}

export function deleteDepartment(data) {
  return request({
    url: '/saas/department/delete',
    method: 'post',
    data
  })
}
