export async function fetchLogin(_data?: unknown) {
  return {
    data: {
      access_token: '',
      refresh_token: ''
    } as Api.Auth.LoginToken,
    error: null
  };
}

export async function fetchLogout() {
  return { data: null, error: null };
}

export async function fetchGetUserInfo() {
  return {
    data: {
      user: undefined,
      roles: [],
      permissions: []
    },
    error: null
  };
}

export async function fetchTenantList() {
  return {
    data: {
      tenantEnabled: false,
      voList: [] as any[]
    },
    error: null
  };
}
