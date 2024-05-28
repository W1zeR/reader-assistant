import React from "react";
import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownTrigger } from "@nextui-org/react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import useAuth from "@/utils/useAuth";

export default function SortOrder() {
  const [selectedKeys, setSelectedKeys] =
    React.useState(new Set(["Сначала новые"]));

  const selectedValue = React.useMemo(
    () => Array.from(selectedKeys).join(", ").replaceAll("_", " "),
    [selectedKeys]
  );

  const pathname = usePathname();
  const searchParams = useSearchParams();
  const { replace } = useRouter();

  const createPageURL = (key: number | string) => {
    const params = new URLSearchParams(searchParams);

    params.set("sort", getSortOrder(key.toString()));
    replace(`${pathname}?${params.toString()}`);
  };

  const isAuthenticated = useAuth(false);

  const getSortOrder = (keyStr: string) => {
    if (keyStr === "Сначала популярные") {
      return "likesCount";
    }
    if (isAuthenticated && keyStr === "Сначала интересные") {
      return "interesting";
    }
    return "changeDate";
  };

  return (
    <Dropdown>
      <DropdownTrigger>
        <Button
          variant="bordered"
          className="rounded-md border border-gray-700 dark:border-gray-300"
        >
          {selectedValue}
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        variant="flat"
        disallowEmptySelection
        selectionMode="single"
        selectedKeys={selectedKeys}
        onSelectionChange={setSelectedKeys}
        onAction={(key) => createPageURL(key)}
      >
        <DropdownItem key="Сначала новые">Сначала новые</DropdownItem>
        <DropdownItem key="Сначала популярные">Сначала популярные</DropdownItem>
        {isAuthenticated ?
          <DropdownItem key="Сначала интересные">Сначала интересные</DropdownItem> : null
        }
      </DropdownMenu>
    </Dropdown>
  );
}
