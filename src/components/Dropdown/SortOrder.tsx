import React from "react";
import {Dropdown, DropdownTrigger, DropdownMenu, DropdownItem, Button} from "@nextui-org/react";
import { ChevronDownIcon } from "@heroicons/react/24/outline";
import { usePathname, useRouter, useSearchParams } from "next/navigation";

export default function SortOrder() {
  const [selectedKeys, setSelectedKeys] = React.useState(new Set(["Сначала новые"]));

  const selectedValue = React.useMemo(
    () => Array.from(selectedKeys).join(", ").replaceAll("_", " "),
    [selectedKeys]
  );

  const pathname = usePathname();
  const searchParams = useSearchParams();
  const { replace } = useRouter();

  const createPageURL = (key: number | string) => {
    const params = new URLSearchParams(searchParams);

    params.set('sort', getSortOrder(key.toString()));
    replace(`${pathname}?${params.toString()}`);
  };

  const getSortOrder = (keyStr: string) => {
    if (keyStr === "Сначала популярные"){
      return "likesCount"
    }
    if (keyStr === "Сначала интересные"){
      return "interesting"
    }
    return "changeDate"
  }

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
        <DropdownItem key="Сначала интересные">Сначала интересные</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );
}
