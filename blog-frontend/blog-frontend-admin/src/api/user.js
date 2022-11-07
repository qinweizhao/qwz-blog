import service from '@/utils/service'

const baseUrl = '/api/admin/user'

const userApi = {}

userApi.getProfile = () => {
  return service({
    url: `${baseUrl}/profiles`,
    method: 'get'
  })
}

userApi.updateProfile = profile => {
  return service({
    url: `${baseUrl}/profiles`,
    method: 'put',
    data: profile
  })
}

userApi.updatePassword = param => {
  return service({
    url: `${baseUrl}/profiles/password`,
    method: 'put',
    data: {
      oldPassword: param.oldPassword,
      newPassword: param.newPassword
    }
  })
}

export default userApi
