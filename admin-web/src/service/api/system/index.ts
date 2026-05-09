const emptyResult = { data: [] as any[], error: null };

export async function fetchGetDeptTree() {
  return emptyResult;
}

export async function fetchGetMenuList() {
  return emptyResult;
}

export async function fetchGetMenuTreeSelect() {
  return emptyResult;
}

export async function fetchGetPostSelect(_deptId?: CommonType.IdType) {
  return emptyResult;
}

export async function fetchGetRoleSelect() {
  return emptyResult;
}

export async function fetchGetUserSelect() {
  return emptyResult;
}

export async function fetchGetDictDataByType(_dictType: string) {
  return emptyResult;
}
